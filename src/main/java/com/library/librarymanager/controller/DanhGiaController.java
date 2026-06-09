package com.library.librarymanager.controller;

import com.library.librarymanager.entity.DanhGia;
import com.library.librarymanager.service.Interface.DanhGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/danh-gia")
@RequiredArgsConstructor
public class DanhGiaController {

    private final DanhGiaService danhGiaService;

    @GetMapping
    @PreAuthorize("hasAuthority('QUAN_LY_KHACH_HANG')")
    public List<DanhGia> getAll() {
        return danhGiaService.getAll();
    }

    @PutMapping("/{id}/reply")
    @PreAuthorize("hasAuthority('QUAN_LY_KHACH_HANG')")
    public DanhGia reply(@PathVariable int id, @RequestParam String reply) {
        return danhGiaService.reply(id, reply);
    }

    @PutMapping("/{id}/reply-with-name")
    @PreAuthorize("hasAuthority('QUAN_LY_KHACH_HANG')")
    public DanhGia replyWithName(@PathVariable int id, @RequestParam String reply, @RequestParam(required = false) String adminName) {
        return danhGiaService.reply(id, reply, adminName);
    }

    @PutMapping("/{id}/duyet")
    @PreAuthorize("hasAuthority('QUAN_LY_KHACH_HANG')")
    public DanhGia duyetDanhGia(@PathVariable int id) {
        return danhGiaService.duyetDanhGia(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('QUAN_LY_KHACH_HANG')")
    public void xoaDanhGia(@PathVariable int id) {
        danhGiaService.xoaDanhGia(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('QUAN_LY_KHACH_HANG')")
    public DanhGia taoDanhGia(
            @RequestParam int khachHangId,
            @RequestParam(required = false) Integer sachId,
            @RequestParam int diemSao,
            @RequestParam String noiDung,
            @RequestParam String loai
    ) {
        return danhGiaService.taoDanhGia(khachHangId, sachId, diemSao, noiDung, loai);
    }

    @PostMapping("/public/submit")
    public DanhGia submitPublicReview(
            @RequestParam String hoTen,
            @RequestParam String sdt,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer sachId,
            @RequestParam int diemSao,
            @RequestParam String noiDung,
            @RequestParam(defaultValue = "SACH") String loai
    ) {
        return danhGiaService.submitPublicReview(hoTen, sdt, email, sachId, diemSao, noiDung, loai);
    }
}
