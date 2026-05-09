package com.library.librarymanager.service;

import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.entity.Sach;

import java.util.List;

public interface KhachHangService {
    List<KhachHang> getAll();
    KhachHang getById(int id);
    KhachHang create(KhachHang khachHang);
    KhachHang updateById(int id,KhachHang khachHang);
    void deleteById(int id);
}
