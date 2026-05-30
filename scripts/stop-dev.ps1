$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
$pidFile = Join-Path $root ".runtime\dev-processes.json"

if (-not (Test-Path $pidFile)) {
    Write-Host "No existe archivo de procesos activos."
    exit 0
}

$items = Get-Content $pidFile | ConvertFrom-Json

foreach ($item in $items) {
    try {
        $process = Get-Process -Id $item.pid -ErrorAction SilentlyContinue
        if ($process) {
            Stop-Process -Id $item.pid -Force
            Write-Host ("Detenido {0} (pid {1})" -f $item.name, $item.pid)
        }

        if ($item.port) {
            Start-Sleep -Milliseconds 600
            $listener = Get-NetTCPConnection -LocalPort $item.port -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1
            if ($listener) {
                Stop-Process -Id $listener.OwningProcess -Force
                Write-Host ("Detenido {0} por puerto {1} (pid {2})" -f $item.name, $item.port, $listener.OwningProcess)
            }
        }
    }
    catch {
        Write-Host ("No fue posible detener {0} (pid {1})" -f $item.name, $item.pid)
    }
}

Remove-Item $pidFile -Force
