package com.library.librarymanager.service;

import com.library.librarymanager.entity.TheLoai;

import java.util.List;

public interface TheLoaiService {
    List<TheLoai> getAll();
    TheLoai getById(int id);
    TheLoai create(TheLoai theLoai);
    TheLoai updateById(int id,TheLoai theLoai);
    void deleteById(int id);
}
