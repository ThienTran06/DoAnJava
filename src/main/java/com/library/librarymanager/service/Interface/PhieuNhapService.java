package com.library.librarymanager.service.Interface;

import com.library.librarymanager.dto.request.PhieuNhapRequest;
import com.library.librarymanager.dto.request.UpdatePhieuNhapRequest;
import com.library.librarymanager.entity.PhieuNhap;

import java.util.List;

public interface PhieuNhapService {
    List<PhieuNhap> getAll();
    PhieuNhap getById(int id);
    PhieuNhap create(PhieuNhapRequest phieuNhap);
    PhieuNhap updateById(int id, UpdatePhieuNhapRequest request);
    PhieuNhap updateChiTiet(int id, PhieuNhapRequest request);
    void huyPhieuNhap(int id);
    void deleteById(int id);
}
