package com.library.librarymanager.job;

import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.repository.KhachHangRepository;
import com.library.librarymanager.service.Interface.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KhachHangDiemScheduler {
    private final KhachHangRepository khachHangRepository;
    private final KhachHangService khachHangService;

    @Transactional
    @Scheduled(cron = "0 0 0 1 * *", zone = "Asia/Ho_Chi_Minh")
    public void giamDiemTichLuyHangThang() {
        List<KhachHang> danhSachKhachHang = khachHangRepository.findAll();

        for (KhachHang khachHang : danhSachKhachHang) {
            int diemSauKhiGiam = BigDecimal.valueOf(khachHang.getDiemTichLuy())
                    .multiply(BigDecimal.valueOf(0.9))
                    .setScale(0, RoundingMode.DOWN)
                    .intValue();
            khachHang.setDiemTichLuy(diemSauKhiGiam);
            khachHangService.capNhatHangThanhVien(khachHang);
        }

        khachHangRepository.saveAll(danhSachKhachHang);
    }
}
