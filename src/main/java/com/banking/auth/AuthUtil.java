package com.banking.auth;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class AuthUtil {

    // Load environment variables from .env
    private static final Dotenv dotenv = Dotenv.load();

    // Read the secret from .env
    private static final String SECRET = dotenv.get("JWT_SECRET");

    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION = 6 * 60 * 60 * 1000L; // 6 hours

    public static String generateToken(String username, String aadhar, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .claim("aadhar", aadhar)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    public static Jws<Claims> parseToken(String jwt) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(jwt);
    }
}
