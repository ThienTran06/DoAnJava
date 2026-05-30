package com.library.librarymanager.controller;

import com.library.librarymanager.dto.response.*;


import com.library.librarymanager.service.Interface.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bao-cao")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('XEM_BAO_CAO')")
public class BaoCaoController {
    private final ChiTietHoaDonService chiTietHoaDonService;
    private final SachService sachService;
    private final HoaDonService hoaDonService;

    @GetMapping("/sach-ban-chay")
    List<SachBanChayResponse> getSachBanChay() {
        return chiTietHoaDonService.getSachBanChay();
    }

    @GetMapping("/ton-kho-nhieu-nhat")
    List<SachTonKhoResponse> getTonKhoNhieuNhat() {
        return sachService.getTonKhoNhieuNhat();
    }

    @GetMapping("/doanh-thu-thang")
    List<DoanhThuThangResponse> getDoanhThuThang(@RequestParam int nam) {
        return hoaDonService.getDoanhThuTheoThang(nam);
    }

    @GetMapping("/doanh-thu-ngay")
    List<DoanhThuNgayResponse> getDoanhThuNgay(@RequestParam int nam, @RequestParam int thang) {
        return hoaDonService.getDoanhThuTheoNgay(nam, thang);
    }

    @GetMapping("/doanh-thu-nam")
    List<DoanhThuNamResponse> getDoanhThuNam() {
        return hoaDonService.getDoanhThuTheoNam();
    }

    @GetMapping("/doanh-thu-theo-the-loai")
    List<DoanhThuTheoTheLoaiResponse> getDoanhThuTheoTheLoai() {
        return chiTietHoaDonService.getDoanhThuTheoTheLoai();
    }

    @GetMapping("/doanh-thu-hom-nay")
    BigDecimal getDoanhThuHomNay() {
        return hoaDonService.getDoanhThuHomNay();
    }

    @GetMapping("/tong-ton-kho")
    Integer getTongTonKho() {
        return sachService.getTongSoLuongTon();
    }

    @GetMapping("/doanh-thu-bay-ngay-truoc")
    List<DoanhThuNgayResponse> getDoanhThuBayNgayTruoc() {
        LocalDateTime fromNow = LocalDateTime.now().minusDays(7);
        return hoaDonService.getDoanhThuBayNgayTruoc(fromNow);
    }

    @GetMapping("/doanh-thu-ba-muoi-ngay-truoc")
    List<DoanhThuNgayResponse> getDoanhThuBaMuoiNgayTruoc() {
        LocalDateTime fromNow = LocalDateTime.now().minusDays(30);
        return hoaDonService.getDoanhThuBaMuoiNgayTruoc(fromNow);
    }

    @GetMapping("/doanh-thu-tu-khoang-ngay")
    List<DoanhThuNgayResponse> getDoanhThuKhoangNgay(@RequestParam LocalDate tuNgay, @RequestParam LocalDate denNgay) {
        return hoaDonService.getDoanhThuTheoKhoangNgay(tuNgay, denNgay);
    }

    @GetMapping("/tong-doanh-thu")
    BigDecimal getTongDoanhThu() {
        return hoaDonService.getTongDoanhThu();
    }

    @GetMapping("/sach-doanh-thu-cao")
    List<TopSachDoanhThuResponse> getSachTopDoanhThu() {
        return chiTietHoaDonService.getSachTopDoanhThu();
    }
}
