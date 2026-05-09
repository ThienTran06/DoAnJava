package com.library.librarymanager.service;

import com.library.librarymanager.entity.ChiTietHoaDon;
import com.library.librarymanager.entity.ChiTietPhieuNhap;
import com.library.librarymanager.entity.Sach;

import java.util.List;

public interface ChiTietHoaDonService {
    List<ChiTietHoaDon> getAll();
    ChiTietHoaDon getById(int id);
    ChiTietHoaDon create(ChiTietHoaDon chiTietHoaDon);
    ChiTietHoaDon updateById(int id,ChiTietHoaDon chiTietHoaDon);
    void deleteById(int id);
}
