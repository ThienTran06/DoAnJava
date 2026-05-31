package com.library.librarymanager.controller;

import com.library.librarymanager.config.JwtUtil;
import com.library.librarymanager.dto.request.LoginRequest;
import com.library.librarymanager.dto.request.RefreshRequest;
import com.library.librarymanager.dto.response.LoginResponse;
import com.library.librarymanager.entity.NguoiDung;
import com.library.librarymanager.entity.RefreshToken;
import com.library.librarymanager.repository.NguoiDungRepository;
import com.library.librarymanager.service.Interface.AuthService;
import com.library.librarymanager.service.impl.RefreshTokenServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final int REFRESH_TOKEN_MAX_AGE_SECONDS = 7 * 24 * 60 * 60;

    @Autowired
    private AuthService service;

    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;

    @Autowired
    private NguoiDungRepository repo;

    @Autowired
    private JwtUtil jwtutil;

    @Value("${app.auth.refresh-cookie-same-site:None}")
    private String refreshCookieSameSite;

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest req,
            HttpServletResponse response
    ) {

        LoginResponse lr = service.login(req);

        if (lr.getRefreshToken() != null) {
            response.addHeader(
                    HttpHeaders.SET_COOKIE,
                    buildRefreshCookie(lr.getRefreshToken(), REFRESH_TOKEN_MAX_AGE_SECONDS).toString()
            );
            lr.setRefreshToken(null);
        }

        return lr;
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(
            @RequestBody(required = false) RefreshRequest req,
            @CookieValue(value = "refreshToken", required = false) String cookieRefreshToken
    ) {

        String token = resolveRefreshToken(req, cookieRefreshToken);

        if (token == null) {
            throw new RuntimeException("Refresh token khong duoc cung cap");
        }

        RefreshToken rt = refreshTokenService.validate(token);

        NguoiDung user = repo.findByUsername(rt.getUsername())
                .orElseThrow(() -> new RuntimeException("Khong tim thay nguoi dung"));

        List<String> permissions = user.getDsPhanQuyen()
                .stream()
                .map(pq -> pq.getChucNang().getTenChucNang())
                .toList();

        String newAccessToken = jwtutil.generateToken(
                user.getId(),
                user.getUsername(),
                user.getNhom().getTenNhom(),
                permissions
        );

        return new LoginResponse(
                "Refresh thanh cong",
                newAccessToken,
                null
        );
    }

    @PostMapping("/logout")
    public String logout(
            @RequestBody(required = false) RefreshRequest req,
            @CookieValue(value = "refreshToken", required = false) String cookieRefreshToken,
            HttpServletResponse response
    ) {

        String token = resolveRefreshToken(req, cookieRefreshToken);

        if (token != null) {
            refreshTokenService.deleteByToken(token);
        }

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                buildRefreshCookie("", 0).toString()
        );

        return "Logout thanh cong";
    }

    private String resolveRefreshToken(RefreshRequest req, String cookieRefreshToken) {
        if (req != null && req.getRefreshToken() != null && !req.getRefreshToken().isBlank()) {
            return req.getRefreshToken();
        }
        if (cookieRefreshToken != null && !cookieRefreshToken.isBlank()) {
            return cookieRefreshToken;
        }
        return null;
    }

    private ResponseCookie buildRefreshCookie(String value, int maxAgeSeconds) {
        return ResponseCookie.from("refreshToken", value)
                .httpOnly(true)
                .secure(true)
                .sameSite(refreshCookieSameSite)
                .path("/")
                .maxAge(maxAgeSeconds)
                .build();
    }
}
