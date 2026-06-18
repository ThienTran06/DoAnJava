package com.library.librarymanager.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.library.librarymanager.entity.DanhGia;
import com.library.librarymanager.service.Interface.DanhGiaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DanhGiaController {

    private final DanhGiaService danhGiaService;

    @GetMapping("/api/danh-gia")
    @PreAuthorize("hasAuthority('QUAN_LY_KHACH_HANG')")
    public List<DanhGia> getAll() {
        return danhGiaService.getAll();
    }

    @PutMapping("/api/danh-gia/{id}/reply")
    @PreAuthorize("hasAuthority('QUAN_LY_KHACH_HANG')")
    public DanhGia reply(@PathVariable int id, @RequestParam String reply) {
        return danhGiaService.reply(id, reply);
    }

    @PutMapping("/api/danh-gia/{id}/reply-with-name")
    @PreAuthorize("hasAuthority('QUAN_LY_KHACH_HANG')")
    public DanhGia replyWithName(@PathVariable int id, @RequestParam String reply, @RequestParam(required = false) String adminName) {
        return danhGiaService.reply(id, reply, adminName);
    }

    @DeleteMapping("/api/danh-gia/{id}")
    @PreAuthorize("hasAuthority('QUAN_LY_KHACH_HANG')")
    public void xoaDanhGia(@PathVariable int id) {
        danhGiaService.xoaDanhGia(id);
    }

    @PostMapping("/api/danh-gia")
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

    @GetMapping("/api/danh-gia/public")
    public List<DanhGia> getPublicReviews() {
        return danhGiaService.getPublicReviews();
    }

    @PostMapping("/api/danh-gia/public/submit")
    public DanhGia submitPublicReview(
            @RequestParam(required = false) Integer khachHangId,
            @RequestParam String hoTen,
            @RequestParam String sdt,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer sachId,
            @RequestParam int diemSao,
            @RequestParam String noiDung,
            @RequestParam(defaultValue = "SACH") String loai,
            @RequestParam(required = false) List<MultipartFile> hinhAnh
    ) {
        return danhGiaService.submitPublicReview(khachHangId, hoTen, sdt, email, sachId, diemSao, noiDung, loai, hinhAnh);
    }
}
