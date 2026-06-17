package com.library.librarymanager.config;


import com.library.librarymanager.Exception.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            chain.doFilter(req, res);
            return;
        }

        String uri = req.getRequestURI();
        log.debug("JwtFilter incoming request: method={} uri={} remote={}", req.getMethod(), uri, req.getRemoteAddr());

        if (isPublicResource(uri)
                || uri.startsWith("/auth")
                || uri.startsWith("/api/public")
                || uri.startsWith("/api/danh-gia/public")
                || uri.startsWith("/swagger-ui")
                || uri.startsWith("/Create")
                || uri.startsWith("/api/webhook/sepay")
                || uri.startsWith("/ws")
                || uri.startsWith("/api/nguoi-dung/create")

                || uri.startsWith("/v3/api-docs")) {

            log.debug("JwtFilter: allowing public resource {}", uri);
            chain.doFilter(req, res);
            return;
        }

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("JwtFilter: missing or invalid Authorization header for {}", uri);
            res.setStatus(401);
            res.getWriter().write("Thiếu token");
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            log.debug("JwtFilter: token validation failed for {}", uri);
            res.setStatus(401);
            res.getWriter().write("Token không hợp lệ");
            return;
        }

        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);

        List<String> permissions = jwtUtil.extractPermissions(token);

        if (permissions == null) {
            permissions = List.of();
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>(
                permissions.stream()
                        .filter(p -> p != null && !p.isBlank())
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );
        if (role != null && !role.isBlank()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );

        SecurityContextHolder.getContext().setAuthentication(auth);



        chain.doFilter(req, res);
    }

    private boolean isPublicResource(String uri) {
        return "/".equals(uri)
                || uri.endsWith(".html")
                || uri.endsWith(".css")
                || uri.endsWith(".js")
                || uri.endsWith(".png")
                || uri.endsWith(".jpg")
                || uri.endsWith(".jpeg")
                || uri.endsWith(".gif")
                || uri.endsWith(".svg")
                || uri.endsWith(".ico")
                || uri.endsWith(".mp3")
                || uri.startsWith("/pages/")
                || uri.startsWith("/js/")
                || uri.startsWith("/music/")
                || uri.startsWith("/images/")
                || uri.startsWith("/assets/");
    }
}
