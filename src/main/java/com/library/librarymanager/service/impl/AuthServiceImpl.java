package com.library.librarymanager.service.impl;

import com.library.librarymanager.Exception.AuthException;
import com.library.librarymanager.config.JwtUtil;
import com.library.librarymanager.dto.request.LoginRequest;
import com.library.librarymanager.dto.response.LoginResponse;
import com.library.librarymanager.entity.ChiTietHoaDon;
import com.library.librarymanager.entity.NguoiDung;
import com.library.librarymanager.entity.RefreshToken;
import com.library.librarymanager.repository.ChiTietHoaDonRepository;
import com.library.librarymanager.repository.NguoiDungRepository;
import com.library.librarymanager.service.AuthService;

import com.library.librarymanager.service.RefreshTokenService;

import org.springframework.stereotype.Service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private NguoiDungRepository repo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtutil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public LoginResponse login(LoginRequest req) {

        NguoiDung user = repo
                .findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy"));

        if (user == null) {
            throw new AuthException("Sai tài khoản");
        }

        if (!encoder.matches(
                req.getPassword(),
                user.getPassword()
        )) {

            throw new AuthException("Sai mật khẩu");
        }

        List<String> permissions =

                user.getDsPhanQuyen()
                        .stream()
                        .map(pq ->
                                pq.getChucNang()
                                        .getTenChucNang()
                        )
                        .toList();

        String accessToken = jwtutil.generateToken(

                user.getUsername(),

                user.getNhom().getTenNhom(),

                permissions
        );

        RefreshToken refreshToken =
                refreshTokenService.create(
                        user.getUsername()
                );

        return new LoginResponse(
                "Login thành công",
                accessToken,
                refreshToken.getToken()
        );
    }
}
