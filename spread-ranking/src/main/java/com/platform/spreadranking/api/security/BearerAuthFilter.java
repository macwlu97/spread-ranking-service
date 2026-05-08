package com.platform.spreadranking.api.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BearerAuthFilter extends GenericFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String auth = req.getHeader("Authorization");

        if (!"Bearer ABC123".equals(auth)) {
            ((HttpServletResponse) response).setStatus(401);
            return;
        }

        chain.doFilter(request, response);
    }
}