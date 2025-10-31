// AuthController.java (replace existing login handler)
package com.banking.controller;

import com.banking.payloads.CustomerDTO;
import com.banking.service.impl.AuthServiceImpl;
import com.banking.services.AuthService;
import com.banking.auth.AuthUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    private final AuthService authService = new AuthServiceImpl();

    @POST
    @Path("/signup")
    public Response signup(CustomerDTO req) {
        try {

            CustomerDTO created = authService.signup(req);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @POST
    @Path("/login")
    public Response login(LoginRequest req) {
        try {

            String token = authService.login(req.getUsername(), req.getPassword());


            String role = null;
            try {
                Jws<Claims> jws = AuthUtil.parseToken(token);
                Claims claims = jws.getBody();
                role = claims.get("role", String.class);
            } catch (JwtException ex) {

                ex.printStackTrace();
            }


            AuthResponse resp = new AuthResponse(token, role);
            return Response.ok(resp).build();

        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername(){return username;}
        public void setUsername(String u){this.username = u;}
        public String getPassword(){return password;}
        public void setPassword(String p){this.password = p;}
    }

    public static class AuthResponse {
        private String token;
        private String role;
        public AuthResponse(){ }
        public AuthResponse(String token, String role){ this.token = token; this.role = role; }

        public String getToken(){ return token; }
        public void setToken(String t){ this.token = t; }
        public String getRole(){ return role; }
        public void setRole(String r){ this.role = r; }
    }
}
