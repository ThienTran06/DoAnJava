package com.library.librarymanager.service.Interface;

import com.library.librarymanager.dto.request.PhieuNhapRequest;
import com.library.librarymanager.entity.PhieuNhap;

import java.util.List;

public interface PhieuNhapService {
    List<PhieuNhap> getAll();
    PhieuNhap getById(int id);
    PhieuNhap create(PhieuNhapRequest phieuNhap);
    PhieuNhap updateById(int id,PhieuNhap phieuNhap);
    void deleteById(int id);
}
