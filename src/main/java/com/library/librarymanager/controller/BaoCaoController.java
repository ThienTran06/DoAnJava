package com.library.librarymanager.controller;

import com.library.librarymanager.dto.response.DoanhThuNgayResponse;
import com.library.librarymanager.dto.response.DoanhThuThangResponse;
import com.library.librarymanager.dto.response.SachBanChayResponse;
import com.library.librarymanager.dto.response.SachTonKhoResponse;
import com.library.librarymanager.repository.ChiTietHoaDonRepository;
import com.library.librarymanager.repository.HoaDonRepository;
import com.library.librarymanager.repository.SachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bao-cao")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('THONG_KE')")
public class BaoCaoController {
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final SachRepository sachRepository;
    private final HoaDonRepository hoaDonRepository;
    @GetMapping("/sach-ban-chay")
    List<SachBanChayResponse> getSachBanChay(){
        return chiTietHoaDonRepository.sachBanChay();
    }
    @GetMapping("/ton-kho-nhieu-nhat")
    List<SachTonKhoResponse> getTonKhoNhieuNhat(){
        return sachRepository.tonKhoNhieuNhat();
    }
    @GetMapping("/doanh-thu-thang")
    List<DoanhThuThangResponse> getDoanhThuThang(@RequestParam int nam){
        return hoaDonRepository.doanhThuTheoThang(nam);
    }
    @GetMapping("/doanh-thu-ngay")
    List<DoanhThuNgayResponse> getDoanhThuNgay(@RequestParam int nam,@RequestParam int thang){
        return hoaDonRepository.doanhThuTheoNgay(nam,thang);
    }




}
