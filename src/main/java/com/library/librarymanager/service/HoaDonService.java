package com.library.librarymanager.service;

import com.library.librarymanager.dto.request.HoaDonRequest;
import com.library.librarymanager.entity.HoaDon;
import com.library.librarymanager.entity.Sach;

import java.util.List;

public interface HoaDonService {
    List<HoaDon> getAll();
    HoaDon getById(int id);
    HoaDon create(HoaDonRequest request);
    HoaDon updateById(int id,HoaDon hoaDon);
    void deleteById(int id);
    void huyHoaDon(int id);
}
