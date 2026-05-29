param(
    [switch]$FrontendOnly,
    [switch]$UseDocker,
    [switch]$ReinstallFrontend
)

$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
$runtimeDir = Join-Path $root ".runtime"
$logDir = Join-Path $runtimeDir "logs"
$pidFile = Join-Path $runtimeDir "dev-processes.json"

New-Item -ItemType Directory -Force $runtimeDir | Out-Null
New-Item -ItemType Directory -Force $logDir | Out-Null

if ($UseDocker) {
    Push-Location $root
    try {
        docker compose up --build
    }
    finally {
        Pop-Location
    }
    exit 0
}

function Start-BackgroundCommand {
    param(
        [string]$Name,
        [string]$WorkingDirectory,
        [string]$FilePath,
        [string[]]$Arguments,
        [int]$Port
    )

    $logPath = Join-Path $logDir "$Name.log"
    $errorPath = Join-Path $logDir "$Name.error.log"

    $process = Start-Process `
        -FilePath $FilePath `
        -ArgumentList $Arguments `
        -WorkingDirectory $WorkingDirectory `
        -WindowStyle Hidden `
        -RedirectStandardOutput $logPath `
        -RedirectStandardError $errorPath `
        -PassThru

    return [pscustomobject]@{
        name = $Name
        pid = $process.Id
        port = $Port
        log = $logPath
        errorLog = $errorPath
    }
}

function Get-FreePort {
    param([int]$Preferred)

    if (-not (Get-NetTCPConnection -LocalPort $Preferred -ErrorAction SilentlyContinue)) {
        return $Preferred
    }

    for ($candidate = $Preferred + 1; $candidate -lt $Preferred + 20; $candidate++) {
        if (-not (Get-NetTCPConnection -LocalPort $candidate -ErrorAction SilentlyContinue)) {
            return $candidate
        }
    }

    throw "No se encontro puerto libre cerca de $Preferred"
}

function Wait-ForHttp {
    param(
        [string]$Name,
        [string]$Url,
        [int]$TimeoutSeconds = 90
    )

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    do {
        try {
            $response = Invoke-WebRequest -UseBasicParsing -Uri $Url -TimeoutSec 3
            if ($response.StatusCode -ge 200 -and $response.StatusCode -lt 500) {
                return
            }
        }
        catch {
        }
        Start-Sleep -Seconds 2
    } while ((Get-Date) -lt $deadline)

    throw "No fue posible confirmar $Name en $Url"
}

function Resolve-PortProcessId {
    param([int]$Port)

    $listener = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue |
        Select-Object -First 1

    if ($listener) {
        return $listener.OwningProcess
    }

    return $null
}

$processes = @()

if (-not $FrontendOnly) {
    $services = @(
        @{ name = "auth"; module = "auth"; port = 8080; health = "http://localhost:8080/actuator/health" },
        @{ name = "catalogo"; module = "catalogo"; port = 8081; health = "http://localhost:8081/actuator/health" },
        @{ name = "business"; module = "business"; port = 8082; health = "http://localhost:8082/actuator/health" },
        @{ name = "booking"; module = "booking"; port = 8083; health = "http://localhost:8083/actuator/health" },
        @{ name = "notifications"; module = "notifications"; port = 8084; health = "http://localhost:8084/actuator/health" },
        @{ name = "analytics"; module = "analytics"; port = 8085; health = "http://localhost:8085/actuator/health" }
    )

    foreach ($service in $services) {
        if (Get-NetTCPConnection -LocalPort $service.port -ErrorAction SilentlyContinue) {
            Write-Host "Saltando $($service.name): puerto $($service.port) ya esta ocupado."
            continue
        }

        $processes += Start-BackgroundCommand `
            -Name $service.name `
            -WorkingDirectory $root `
            -FilePath (Join-Path $root "gradlew.bat") `
            -Arguments @(":$($service.module):bootRun") `
            -Port $service.port
    }

    foreach ($service in $services) {
        if (-not (Get-NetTCPConnection -LocalPort $service.port -ErrorAction SilentlyContinue) -and ($processes.name -contains $service.name)) {
            Wait-ForHttp -Name $service.name -Url $service.health -TimeoutSeconds 120
        }
    }
}

$frontendDir = Join-Path $root "frontend"
if ($ReinstallFrontend -or -not (Test-Path (Join-Path $frontendDir "node_modules"))) {
    Push-Location $frontendDir
    try {
        npm install
    }
    finally {
        Pop-Location
    }
}

$frontendPort = Get-FreePort -Preferred 3000
$processes += Start-BackgroundCommand `
    -Name "frontend" `
    -WorkingDirectory $frontendDir `
    -FilePath "npm.cmd" `
    -Arguments @("run", "dev", "--", "--port", "$frontendPort") `
    -Port $frontendPort

Wait-ForHttp -Name "frontend" -Url "http://localhost:$frontendPort" -TimeoutSeconds 60

foreach ($item in $processes) {
    $realPid = Resolve-PortProcessId -Port $item.port
    if ($realPid) {
        $item.pid = $realPid
    }
}

$processes | ConvertTo-Json | Set-Content $pidFile

Write-Host ""
Write-Host "Entorno iniciado:"
foreach ($item in $processes) {
    Write-Host ("- {0}: pid {1}, puerto {2}" -f $item.name, $item.pid, $item.port)
}
Write-Host ""
Write-Host "Frontend: http://localhost:$frontendPort"
Write-Host "Logs: $logDir"
