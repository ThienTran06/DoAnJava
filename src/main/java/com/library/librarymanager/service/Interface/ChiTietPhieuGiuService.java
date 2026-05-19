package com.library.librarymanager.service.Interface;

import com.library.librarymanager.entity.ChiTietHoaDon;
import com.library.librarymanager.entity.ChiTietPhieuGiu;

import java.util.List;

public interface  ChiTietPhieuGiuService {

    void themSach(int phieuId, int sachId, int soLuong);

    void xoaSach(int chiTietId);
    List<ChiTietPhieuGiu> getAll();
}