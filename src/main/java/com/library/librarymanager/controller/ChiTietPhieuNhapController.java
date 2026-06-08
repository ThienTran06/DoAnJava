package com.library.librarymanager.controller;

import com.library.librarymanager.entity.ChiTietPhieuNhap;
import com.library.librarymanager.service.Interface.ChiTietPhieuNhapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chi-tiet-phieu-nhap")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('QUAN_LY_PHIEU_NHAP')")
public class ChiTietPhieuNhapController {
    private final ChiTietPhieuNhapService chiTietPhieuNhapService;
    @GetMapping
    List<ChiTietPhieuNhap> getAll(){return chiTietPhieuNhapService.getAll();}
    @GetMapping("/{id}")
    ChiTietPhieuNhap getById(@PathVariable int id){return chiTietPhieuNhapService.getById(id);}
}
