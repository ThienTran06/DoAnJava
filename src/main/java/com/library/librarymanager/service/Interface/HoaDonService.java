package com.library.librarymanager.service.Interface;

import com.library.librarymanager.dto.request.HoaDonRequest;
import com.library.librarymanager.dto.request.UpdateHoaDonRequest;
import com.library.librarymanager.dto.response.*;
import com.library.librarymanager.entity.HoaDon;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface HoaDonService {
    List<HoaDon> getAll();
    Page<HoaDon> getAll(Integer id, LocalDate ngay, int page, int size);
    HoaDon getById(int id);
    HoaDon create(HoaDonRequest request);
    HoaDon updateById(int id, UpdateHoaDonRequest request);
    void deleteById(int id);
    void huyHoaDon(int id);
    List<DoanhThuNgayResponse> getDoanhThuTheoNgay(int nam, int thang);
    List<DoanhThuThangResponse> getDoanhThuTheoThang(int nam);
    List<DoanhThuNamResponse> getDoanhThuTheoNam();
    BigDecimal getDoanhThuHomNay();
    List<DoanhThuNgayResponse> getDoanhThuBayNgayTruoc(LocalDateTime hientai);
    List<DoanhThuNgayResponse> getDoanhThuBaMuoiNgayTruoc(LocalDateTime local);
    List<DoanhThuNgayResponse> getDoanhThuTheoKhoangNgay(LocalDate tuNgay, LocalDate denNgay);
    BigDecimal getTongDoanhThu();
    HoaDon updateChiTiet(int id, HoaDonRequest request);
    ThongKeHoaDonResponse getThongKe();
    void thanhToanTienMat(int hoaDonId);
    void setPendingStatus(int hoaDonId);
    void xacNhanThanhToan(int hoaDonId);
}
