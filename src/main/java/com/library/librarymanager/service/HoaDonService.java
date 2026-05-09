package com.library.librarymanager.service;

import com.library.librarymanager.entity.HoaDon;
import com.library.librarymanager.entity.Sach;

import java.util.List;

public interface HoaDonService {
    List<HoaDon> getAll();
    HoaDon getById(int id);
    HoaDon create(HoaDon hoaDon);
    HoaDon updateById(int id,HoaDon hoaDon);
    void deleteById(int id);
}
