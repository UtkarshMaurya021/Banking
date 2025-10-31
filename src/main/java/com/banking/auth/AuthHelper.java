package com.banking.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.ws.rs.core.HttpHeaders;

public class AuthHelper {

    public static class RequestAuth {
        public String role;
        public String aadhar;
        public String username;
    }

    public static RequestAuth extractAuth(HttpHeaders headers) {
        RequestAuth ra = new RequestAuth();

        String authHeader = headers.getHeaderString("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            try {
                Jws<Claims> parsed = AuthUtil.parseToken(token);
                Claims claims = parsed.getBody();
                ra.username = claims.getSubject();
                ra.aadhar = claims.get("aadhar", String.class);
                ra.role = claims.get("role", String.class);
                return ra;
            } catch (Exception ex) {
                System.out.println("Invalid or expired JWT: " + ex.getMessage());

            }
        }

        ra.role = headers.getHeaderString("X-Requester-Role");
        ra.aadhar = headers.getHeaderString("X-Requester-Aadhar");

        return ra;
    }
}
