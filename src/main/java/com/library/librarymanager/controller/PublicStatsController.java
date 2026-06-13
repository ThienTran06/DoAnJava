package com.library.librarymanager.controller;

import com.library.librarymanager.dto.response.SachThongKeResponse;
import com.library.librarymanager.dto.response.ThongKeHoaDonResponse;
import com.library.librarymanager.entity.DanhGia;
import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.service.Interface.DanhGiaService;
import com.library.librarymanager.service.Interface.HoaDonService;
import com.library.librarymanager.service.Interface.SachService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicStatsController {

    private final SachService sachService;
    private final HoaDonService hoaDonService;
    private final DanhGiaService danhGiaService;

    @GetMapping("/login-stats")
    public Map<String, Object> getLoginStats() {
        SachThongKeResponse sachStats = sachService.getThongKeSach();
        ThongKeHoaDonResponse invoiceStats = hoaDonService.getThongKe();
        BigDecimal todayRevenue = hoaDonService.getDoanhThuHomNay();

        return Map.of(
                "tongDauSach", sachStats.getTongDauSach(),
                "hoaDonTrongNgay", invoiceStats.getHoaDonTrongNgay(),
                "doanhThuHomNay", todayRevenue == null ? BigDecimal.ZERO : todayRevenue
        );
    }

    @GetMapping("/sach")
    public List<Sach> getAllSach() {
        return sachService.getAll();
    }

    @GetMapping("/danh-gia")
    public List<DanhGia> getPublicReviews() {
        return danhGiaService.getPublicReviews();
    }
}
