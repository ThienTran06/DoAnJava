package com.library.librarymanager.service;

import com.library.librarymanager.entity.NhanVien;
import com.library.librarymanager.entity.Sach;

import java.util.List;

public interface NhanVienService {
    List<NhanVien> getAll();
    NhanVien getById(int id);
    NhanVien create(NhanVien nhanVien);
    NhanVien updateById(int id,NhanVien nhanVien);
    void deleteById(int id);
}
