package com.library.librarymanager.service.impl;

import com.library.librarymanager.Exception.AuthException;
import com.library.librarymanager.config.JwtUtil;
import com.library.librarymanager.dto.request.LoginRequest;
import com.library.librarymanager.dto.response.LoginResponse;
import com.library.librarymanager.entity.NguoiDung;
import com.library.librarymanager.entity.RefreshToken;
import com.library.librarymanager.repository.NguoiDungRepository;
import com.library.librarymanager.service.Interface.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private NguoiDungRepository repo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtutil;

    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;

    @Override
    public LoginResponse login(LoginRequest req) {

        NguoiDung user = repo
                .findByUsername(req.getUsername())
                .orElseThrow(() -> new AuthException("Sai tai khoan hoac mat khau"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new AuthException("Sai tai khoan hoac mat khau");
        }

        List<String> permissions = user.getDsPhanQuyen()
                .stream()
                .map(pq -> pq.getChucNang().getTenChucNang())
                .toList();

        String accessToken = jwtutil.generateToken(
                user.getUsername(),
                user.getNhom().getTenNhom(),
                permissions
        );

        RefreshToken refreshToken = refreshTokenService.create(user.getUsername());

        return new LoginResponse(
                "Login thanh cong",
                accessToken,
                refreshToken.getRawToken()
        );
    }
}
