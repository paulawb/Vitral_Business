import { SessionState } from "@/types/auth";
import { getCookie, removeCookie, setCookie } from "@/utils/cookies";

const ACCESS = "vitral_access_token";
const REFRESH = "vitral_refresh_token";
const TENANT = "vitral_tenant_id";
const SESSION = "vitral_session";

export function persistSession(session: SessionState) {
  if (typeof window === "undefined") {
    return;
  }
  window.localStorage.setItem(SESSION, JSON.stringify(session));
  setCookie(ACCESS, session.accessToken, Math.max(60, Math.floor((session.expiresAt - Date.now()) / 1000)));
  setCookie(REFRESH, session.refreshToken, 60 * 60 * 24 * 7);
  setCookie(TENANT, session.tenantId, 60 * 60 * 24 * 7);
}

export function readSession(): SessionState | null {
  if (typeof window === "undefined") {
    return null;
  }
  const raw = window.localStorage.getItem(SESSION);
  if (!raw) {
    return null;
  }
  try {
    return JSON.parse(raw) as SessionState;
  } catch {
    return null;
  }
}

export function clearSessionStorage() {
  if (typeof window !== "undefined") {
    window.localStorage.removeItem(SESSION);
  }
  removeCookie(ACCESS);
  removeCookie(REFRESH);
  removeCookie(TENANT);
}

export function readAccessToken() {
  return getCookie(ACCESS);
}

export function readRefreshToken() {
  return getCookie(REFRESH);
}

export function hasTenant(tenantId?: string | null) {
  return !!tenantId && tenantId !== "UNASSIGNED";
}
