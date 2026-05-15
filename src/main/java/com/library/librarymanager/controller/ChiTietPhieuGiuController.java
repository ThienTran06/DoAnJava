package com.library.librarymanager.controller;

import com.library.librarymanager.service.Interface.ChiTietPhieuGiuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chi-tiet-phieu-giu")
@PreAuthorize("hasAuthority('QUAN_LY_HOA_DON')")
public class ChiTietPhieuGiuController {

    private final ChiTietPhieuGiuService ctService;

    public ChiTietPhieuGiuController(
            ChiTietPhieuGiuService ctService
    ) {
        this.ctService = ctService;
    }

    @PostMapping("/them")
    public String themSach(
            @RequestParam int phieuId,
            @RequestParam int sachId,
            @RequestParam int soLuong
    ) {

        ctService.themSach(
                phieuId,
                sachId,
                soLuong
        );

        return "Them sach thanh cong";
    }

    @DeleteMapping("/xoa/{chiTietId}")
    public String xoaSach(
            @PathVariable int chiTietId
    ) {

        ctService.xoaSach(chiTietId);

        return "Xoa sach thanh cong";
    }
}