package com.library.librarymanager.service.Interface;

import com.library.librarymanager.dto.request.HoaDonRequest;
import com.library.librarymanager.dto.response.DoanhThuNamResponse;
import com.library.librarymanager.dto.response.DoanhThuNgayResponse;
import com.library.librarymanager.dto.response.DoanhThuThangResponse;
import com.library.librarymanager.dto.response.DoanhThuTheoTheLoaiResponse;
import com.library.librarymanager.entity.HoaDon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface HoaDonService {
    List<HoaDon> getAll();
    HoaDon getById(int id);
    HoaDon create(HoaDonRequest request);
    HoaDon updateById(int id,HoaDon hoaDon);
    void deleteById(int id);
    void huyHoaDon(int id);
    List<DoanhThuNgayResponse> getDoanhThuTheoNgay(int nam, int thang);
    List<DoanhThuThangResponse> getDoanhThuTheoThang(int nam);
    List<DoanhThuNamResponse> getDoanhThuTheoNam();
    BigDecimal getDoanhThuHomNay();
    List<DoanhThuNgayResponse> getDoanhThuBayNgayTruoc(LocalDateTime hientai);
    List<DoanhThuNgayResponse> getDoanhThuBaMuoiNgayTruoc(LocalDateTime local);
}
