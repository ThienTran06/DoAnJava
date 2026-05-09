package com.library.librarymanager.controller;

import com.library.librarymanager.config.JwtUtil;
import com.library.librarymanager.dto.response.AuthResponse;
import com.library.librarymanager.dto.request.LoginRequest;
import com.library.librarymanager.dto.request.RegisterRequest;
import com.library.librarymanager.entity.NhanVien;
import com.library.librarymanager.repository.NhanVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final NhanVienRepository nhanVienRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest){
        if(nhanVienRepository.findByUsername(registerRequest.getUsername()).isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username này đã có người dùng,xin chọn username khác!");
        NhanVien newNhanVien = new NhanVien();
        newNhanVien.setHoTen(registerRequest.getHoTen());
        newNhanVien.setSDT(registerRequest.getSdt());
        newNhanVien.setUsername(registerRequest.getUsername());
        newNhanVien.setTrangThai(true);
        newNhanVien.setVaiTro(registerRequest.getVaiTro());
        newNhanVien.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        nhanVienRepository.save(newNhanVien);
        return ResponseEntity.ok("Đăng ký thành công!");
    }
    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        NhanVien nhanVien = nhanVienRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với username này!"));
        String token = jwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(new AuthResponse(token,nhanVien.getHoTen(), nhanVien.getVaiTro()));
    }


}
