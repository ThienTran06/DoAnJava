package com.library.librarymanager.controller;

import com.library.librarymanager.entity.HoaDon;
import com.library.librarymanager.entity.PhieuDatGiuSach;
import com.library.librarymanager.service.Interface.PhieuGiuSachService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/phieu-giu")
@PreAuthorize("hasAuthority('QUAN_LY_HOA_DON')")
public class PhieuGiuSachController {

    private final PhieuGiuSachService phieuService;

    public PhieuGiuSachController(
            PhieuGiuSachService phieuService
    ) {
        this.phieuService = phieuService;
    }

    @PostMapping("/tao")
    public int taoPhieu(@RequestParam int khachHangId) {

        return phieuService.taoPhieu(khachHangId);
    }

    @PostMapping("/tao/{khachHangId}")
    public int taoPhieuByPath(@PathVariable int khachHangId) {

        return phieuService.taoPhieu(khachHangId);
    }

    @PostMapping("/confirm/{phieuId}")
    public HoaDon confirm(
            @PathVariable int phieuId,
            @PathVariable int nhanVienId
    ) {
        return phieuService.confirm(phieuId, nhanVienId);
    }

    @PostMapping("/confirm/{phieuId}/{nhanVienId}")
    public HoaDon confirmByPath(
            @PathVariable int phieuId,
            @PathVariable int nhanVienId
    ) {



        return    phieuService.confirm(phieuId, nhanVienId);
    }
    @GetMapping
    public ResponseEntity<Page<PhieuDatGiuSach>> getAll(
            @RequestParam(required = false) Integer ma,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate ngay,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                phieuService.getAll(ma, ngay, page, size)
        );
    }


    @PostMapping("/huy/{phieuId}")
    public String huy(@PathVariable int phieuId) {

        phieuService.huy(phieuId);

        return "Huy thanh cong";
    }

    @PostMapping("/expire/{phieuId}")
    public String expire(@PathVariable int phieuId) {

        phieuService.expire(phieuId);

        return "Expire thanh cong";
    }
    @GetMapping("/{id}")
    PhieuDatGiuSach getById(@PathVariable int id){
        return phieuService.getById(id);
    }
}
