package com.library.librarymanager.controller;

import com.library.librarymanager.entity.HoaDon;
import com.library.librarymanager.entity.PhieuDatGiuSach;
import com.library.librarymanager.service.Interface.PhieuGiuSachService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/confirm/{phieuId}")
    public String confirm(
            @PathVariable int phieuId,
            @RequestParam int nhanVienId
    ) {

        phieuService.confirm(phieuId, nhanVienId);

        return "Xac nhan thanh cong";
    }
    @GetMapping
    List<PhieuDatGiuSach> getAll(){return phieuService.getAll();}

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
}