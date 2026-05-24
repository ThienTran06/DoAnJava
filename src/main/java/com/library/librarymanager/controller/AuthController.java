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
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;

    @Autowired
    private NguoiDungRepository repo;

    @Autowired
    private JwtUtil jwtutil;

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest req,
            HttpServletResponse response
    ) {

        // call service to authenticate and generate tokens
        LoginResponse lr = service.login(req);

        // set refresh token as HttpOnly cookie (7 days)
        if (lr.getRefreshToken() != null) {
            Cookie cookie = new Cookie("refreshToken", lr.getRefreshToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
            response.addCookie(cookie);
        }

        return lr;
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(
            @RequestBody(required = false) RefreshRequest req,
            @CookieValue(value = "refreshToken", required = false) String cookieRefreshToken
    ) {

        String token = null;

        if (req != null && req.getRefreshToken() != null && !req.getRefreshToken().isBlank()) {
            token = req.getRefreshToken();
        } else if (cookieRefreshToken != null && !cookieRefreshToken.isBlank()) {
            token = cookieRefreshToken;
        }

        if (token == null) {
            throw new RuntimeException("Refresh token không được cung cấp");
        }

        RefreshToken rt = refreshTokenService.validate(token);

        NguoiDung user = repo.findByUsername(rt.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        List<String> permissions = user.getDsPhanQuyen()
                .stream()
                .map(pq -> pq.getChucNang().getTenChucNang())
                .toList();

        String newAccessToken = jwtutil.generateToken(
                user.getUsername(),
                user.getNhom().getTenNhom(),
                permissions
        );

        return new LoginResponse(
                "Refresh thành công",
                newAccessToken,
                token
        );
    }

    @PostMapping("/logout")
    public String logout(
            @RequestBody(required = false) RefreshRequest req,
            @CookieValue(value = "refreshToken", required = false) String cookieRefreshToken,
            HttpServletResponse response
    ) {

        String token = null;

        if (req != null && req.getRefreshToken() != null && !req.getRefreshToken().isBlank()) {
            token = req.getRefreshToken();
        } else if (cookieRefreshToken != null && !cookieRefreshToken.isBlank()) {
            token = cookieRefreshToken;
        }

        if (token != null) {
            refreshTokenService.deleteByToken(token);

            // clear cookie
            Cookie cookie = new Cookie("refreshToken", "");
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        return "Logout thành công";
    }
}
