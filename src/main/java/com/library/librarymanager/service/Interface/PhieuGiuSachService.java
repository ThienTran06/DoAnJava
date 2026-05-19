package com.library.librarymanager.service.Interface;


import com.library.librarymanager.entity.PhieuDatGiuSach;

import java.util.List;

public interface PhieuGiuSachService {

    int taoPhieu(int khachHangId);

    void confirm(int phieuId,int NguoiDungId);

    void expire(int phieuId);

    void huy(int phieuId);
    List<PhieuDatGiuSach> getAll();
}