package com.platform.spreadranking.api.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BearerAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String auth = req.getHeader("Authorization");

        if (req.getRequestURI().startsWith("/api/")) {

            if (auth == null || !auth.equals("Bearer ABC123")) {
                ((HttpServletResponse) response).setStatus(401);
                return;
            }
        }

        chain.doFilter(request, response);
    }
}