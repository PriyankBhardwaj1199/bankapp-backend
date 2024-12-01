package com.app.bank.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // Set custom response status and content type
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // HTTP 403 Forbidden
        response.setContentType("application/json");

        // Write a custom error response with a code
        response.getWriter().write("{"
                + "\"responseCode\": \"403\","
                + "\"error\": \"Access Denied\","
                + "\"message\": \"You do not have permission to access this resource.\""
                + "}");
    }
}

