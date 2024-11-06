package com.example.tacocloudapplication.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SecurityContextDebugFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            System.out.println("Authorities in SecurityContext: " +
                    SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        } else {
            System.out.println("SecurityContext has no authentication set");
        }
        filterChain.doFilter(request, response);
    }
}
