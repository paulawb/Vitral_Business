package com.vitral.auth.infraestructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public final class JwtUtil {

    private static final String SECRET = "vitral_super_secret_key_for_jwt_auth_2026";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;
    private static final long REFRESH_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;

    private JwtUtil() {
    }

    public static String generateToken(String correo, String tenantId) {
        return Jwts.builder()
                .setSubject(correo)
                .claim("tenantId", tenantId == null ? "UNASSIGNED" : tenantId)
                .claim("rol", "cliente") // Default role for vitrina-only use case
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String generateToken(String correo) {
        return generateToken(correo, "default");
    }

    public static String generateRefreshToken(String correo, String tenantId) {
        return Jwts.builder()
                .setSubject(correo)
                .claim("tenantId", tenantId == null ? "UNASSIGNED" : tenantId)
                .claim("rol", "cliente") // Default role for vitrina-only use case
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String validateToken(String token) {
        try {
            return parseClaims(token).getSubject();
        } catch (Exception exception) {
            return null;
        }
    }

    public static String getClaim(String token, String keyName) {
        try {
            Object value = parseClaims(token).get(keyName);
            return value == null ? null : value.toString();
        } catch (Exception exception) {
            return null;
        }
    }

    public static long getExpirationTimeSeconds() {
        return EXPIRATION_TIME / 1000;
    }

    private static Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
