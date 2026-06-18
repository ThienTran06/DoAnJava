package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.KhachHangLoginRequest;
import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.service.Interface.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/khach-hang")
@RequiredArgsConstructor
public class KhachHangAuthController {

    private final KhachHangService khachHangService;

    @PostMapping("/login")
    public KhachHang login(@RequestBody(required = false) KhachHangLoginRequest request) {
        String email = request == null ? null : request.getEmail();
        String password = request == null ? null : request.getPassword();
        return khachHangService.login(email, password);
    }
}
