package com.library.librarymanager.service.Interface;

import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.entity.MaGiamGia;

import java.math.BigDecimal;
import java.util.List;

public interface MaGiamGiaService {
    List<MaGiamGia> getAll();

    MaGiamGia doiDiem(int khachHangId, int diemSuDung);

    List<MaGiamGia> getByKhachHangId(int khachHangId);

    MaGiamGia suDungMa(KhachHang khachHang, String ma);

    BigDecimal tinhTienSauMaGiamGia(BigDecimal tongTien, MaGiamGia maGiamGia);

    void deleteUsedById(int id);
}
