package com.banking.config;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) { }

    @Override
    public void destroy() { }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");

        if (origin != null && (
                origin.equals("http://localhost:4200") ||
                        origin.equals("https://localhost:4200") ||
                        origin.equals("http://127.0.0.1:4200") ||
                        origin.endsWith(".builder.io") ||
                        origin.endsWith(".builder.my") ||
                        origin.endsWith(".builder.codes") ||
                        origin.contains("ngrok.io")
        )) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        response.setHeader("Vary", "Origin");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");


        response.setHeader("Access-Control-Allow-Headers",
                "Content-Type, Authorization, X-Requester-Role, X-Requester-Aadhar, X-Requested-With, Accept, Origin");

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "3600");


        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(req, res);
    }
}
