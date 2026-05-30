package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.KpiNgay;
import com.library.librarymanager.repository.KpiNgayRepository;
import com.library.librarymanager.service.Interface.KpiNgayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KpiNgayServiceImpl implements KpiNgayService {
    private final KpiNgayRepository kpiNgayRepository;

    @Override
    public KpiNgay getHomNay() {
        return kpiNgayRepository.findByNgay(LocalDate.now()).orElse(null);
    }

    @Override
    @Transactional
    public KpiNgay datKpiHomNay(BigDecimal mucTieuDoanhThu) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        KpiNgay kpi = kpiNgayRepository.findByNgay(today).orElseGet(() -> {
            KpiNgay item = new KpiNgay();
            item.setNgay(today);
            item.setCreatedAt(now);
            return item;
        });

        kpi.setMucTieuDoanhThu(mucTieuDoanhThu);
        kpi.setUpdatedAt(now);

        return kpiNgayRepository.save(kpi);
    }
}
