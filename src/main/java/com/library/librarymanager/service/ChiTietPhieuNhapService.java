package com.library.librarymanager.service;

import com.library.librarymanager.entity.ChiTietPhieuNhap;
import com.library.librarymanager.entity.Sach;

import java.util.List;

public interface ChiTietPhieuNhapService {
    List<ChiTietPhieuNhap> getAll();
    ChiTietPhieuNhap getById(int id);
    ChiTietPhieuNhap create(ChiTietPhieuNhap chiTietPhieuNhap);
    ChiTietPhieuNhap updateById(int id,ChiTietPhieuNhap chiTietPhieuNhap);
    void deleteById(int id);
}
