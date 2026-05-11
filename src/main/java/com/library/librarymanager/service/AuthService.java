package com.library.librarymanager.service;

import com.library.librarymanager.dto.request.LoginRequest;
import com.library.librarymanager.dto.response.LoginResponse;
import com.library.librarymanager.entity.ChiTietHoaDon;
import com.library.librarymanager.entity.ChiTietPhieuNhap;
import com.library.librarymanager.entity.Sach;

import java.util.List;

public interface AuthService {    LoginResponse login(LoginRequest req);

}
