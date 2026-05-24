package com.library.librarymanager.service.Interface;

import com.library.librarymanager.dto.response.DoanhThuTheoTheLoaiResponse;
import com.library.librarymanager.dto.response.SachBanChayResponse;
import com.library.librarymanager.entity.ChiTietHoaDon;

import java.util.List;

public interface ChiTietHoaDonService {
    List<ChiTietHoaDon> getAll();
    ChiTietHoaDon getById(int id);
    ChiTietHoaDon create(ChiTietHoaDon chiTietHoaDon);
    ChiTietHoaDon updateById(int id,ChiTietHoaDon chiTietHoaDon);
    void deleteById(int id);
    List<SachBanChayResponse> getSachBanChay();
    List<DoanhThuTheoTheLoaiResponse>getDoanhThuTheoTheLoai();

}
