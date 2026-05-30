package com.library.librarymanager.service.Interface;

import com.library.librarymanager.entity.KpiNgay;

import java.math.BigDecimal;

public interface KpiNgayService {
    KpiNgay getHomNay();
    KpiNgay datKpiHomNay(BigDecimal mucTieuDoanhThu);
}
