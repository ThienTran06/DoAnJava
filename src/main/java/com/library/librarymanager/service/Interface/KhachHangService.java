package com.library.librarymanager.service.Interface;

import com.library.librarymanager.entity.KhachHang;

import java.util.List;

public interface KhachHangService {
    List<KhachHang> getAll();
    KhachHang getById(int id);
    KhachHang create(KhachHang khachHang);
    KhachHang updateById(int id,KhachHang khachHang);
    void deleteById(int id);
}
