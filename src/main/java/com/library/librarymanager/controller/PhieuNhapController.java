package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.PhieuNhapRequest;
import com.library.librarymanager.dto.response.ThongKePhieuNhapResponse;
import com.library.librarymanager.entity.PhieuNhap;
import com.library.librarymanager.service.Interface.PhieuNhapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/phieu-nhap")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('QUAN_LY_PHIEU_NHAP')")
public class PhieuNhapController {
    private final PhieuNhapService phieuNhapService;
    @GetMapping
    public ResponseEntity<Page<PhieuNhap>> getAll(
            @RequestParam(required = false) Integer ma,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate ngay,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                phieuNhapService.getAll(ma, ngay, page, size)
        );
    }
    @GetMapping("/{id}")
    PhieuNhap getById(@PathVariable int id){return phieuNhapService.getById(id);}
    @PostMapping
    PhieuNhap create(@RequestBody PhieuNhapRequest request){return  phieuNhapService.create(request);}
    @PutMapping("/{id}")
    PhieuNhap updateById(@PathVariable int id, @RequestBody PhieuNhap phieuNhap){return phieuNhapService.updateById(id,phieuNhap);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){phieuNhapService.deleteById(id);}
    @GetMapping("/thong_ke")
    public ResponseEntity<ThongKePhieuNhapResponse> getDashboard() {
        return ResponseEntity.ok(phieuNhapService.getThongKe());
    }
}
