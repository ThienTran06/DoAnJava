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

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
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
            @Valid @RequestBody LoginRequest req
    ) {
        return service.login(req);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(
            @RequestBody RefreshRequest req
    ) {

        RefreshToken rt =
                refreshTokenService.validate(
                        req.getRefreshToken()
                );

        NguoiDung user =
                repo.findByUsername(
                        rt.getUsername()
                ).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        List<String> permissions =
                user.getDsPhanQuyen()
                        .stream()
                        .map(pq ->
                                pq.getChucNang()
                                        .getTenChucNang()
                        )
                        .toList();

        String newAccessToken =
                jwtutil.generateToken(
                        user.getUsername(),
                        user.getNhom().getTenNhom(),
                        permissions
                );

        return new LoginResponse(
                "Refresh thành công",
                newAccessToken,
                req.getRefreshToken()
        );
    }

    @PostMapping("/logout")
    public String logout(
            @RequestBody RefreshRequest req
    ) {

        refreshTokenService.deleteByToken(
                req.getRefreshToken()
        );

        return "Logout thành công";
    }
}