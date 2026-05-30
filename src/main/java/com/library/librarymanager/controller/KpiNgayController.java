package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.KpiNgayRequest;
import com.library.librarymanager.entity.KpiNgay;
import com.library.librarymanager.service.Interface.KpiNgayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kpi-ngay")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('XEM_BAO_CAO')")
public class KpiNgayController {
    private final KpiNgayService kpiNgayService;

    @GetMapping("/hom-nay")
    public KpiNgay getHomNay() {
        return kpiNgayService.getHomNay();
    }

    @PostMapping("/hom-nay")
    public KpiNgay datKpiHomNay(@Valid @RequestBody KpiNgayRequest request) {
        return kpiNgayService.datKpiHomNay(request.getMucTieuDoanhThu());
    }
}
