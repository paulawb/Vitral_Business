import { NextRequest, NextResponse } from "next/server";

const protectedPaths = ["/dashboard", "/analytics", "/admin", "/configuracion", "/planes", "/onboarding", "/citas", "/catalogo"];
const authPaths = ["/login", "/registro"];

export function middleware(request: NextRequest) {
  const accessToken = request.cookies.get("vitral_access_token")?.value;
  const tenantId = request.cookies.get("vitral_tenant_id")?.value;
  const { pathname } = request.nextUrl;

  const isProtected = protectedPaths.some((path) => pathname === path || pathname.startsWith(`${path}/`));
  const isAuthPage = authPaths.includes(pathname);
  const needsOnboarding = !!accessToken && (!tenantId || tenantId === "UNASSIGNED");

  if (isProtected && !accessToken && pathname !== "/login") {
    const loginUrl = new URL("/login", request.url);
    loginUrl.searchParams.set("redirect", pathname);
    return NextResponse.redirect(loginUrl);
  }

  if (isAuthPage && accessToken) {
    return NextResponse.redirect(new URL(needsOnboarding ? "/onboarding" : "/dashboard", request.url));
  }

  if (accessToken && needsOnboarding && pathname !== "/onboarding") {
    return NextResponse.redirect(new URL("/onboarding", request.url));
  }

  return NextResponse.next();
}

export const config = {
  matcher: ["/dashboard/:path*", "/analytics/:path*", "/admin/:path*", "/configuracion/:path*", "/planes/:path*", "/onboarding", "/login", "/registro", "/citas/:path*", "/catalogo/:path*"]
};
