package com.library.librarymanager.service.Interface;


import com.library.librarymanager.entity.HoaDon;
import com.library.librarymanager.entity.PhieuDatGiuSach;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface PhieuGiuSachService {

    int taoPhieu(int khachHangId);

    HoaDon confirm(int phieuId, int nhanVienId);

    void expire(int phieuId);

    void huy(int phieuId);

    PhieuDatGiuSach getById(int id);
    Page<PhieuDatGiuSach> getAll(Integer ma, LocalDate ngay, int page, int size);
}