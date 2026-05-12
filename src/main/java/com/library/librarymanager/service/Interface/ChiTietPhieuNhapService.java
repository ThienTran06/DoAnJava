package com.library.librarymanager.service.Interface;

import com.library.librarymanager.entity.ChiTietPhieuNhap;

import java.util.List;

public interface ChiTietPhieuNhapService {
    List<ChiTietPhieuNhap> getAll();
    ChiTietPhieuNhap getById(int id);
    ChiTietPhieuNhap create(ChiTietPhieuNhap chiTietPhieuNhap);
    ChiTietPhieuNhap updateById(int id,ChiTietPhieuNhap chiTietPhieuNhap);
    void deleteById(int id);
}
