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
    @PostMapping
    ChiTietPhieuNhap create(@RequestBody ChiTietPhieuNhap chiTietPhieuNhap){return  chiTietPhieuNhapService.create(chiTietPhieuNhap);}
    @PutMapping("/{id}")
    ChiTietPhieuNhap updateById(@PathVariable int id, @RequestBody ChiTietPhieuNhap chiTietPhieuNhap){return chiTietPhieuNhapService.updateById(id,chiTietPhieuNhap);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){chiTietPhieuNhapService.deleteById(id);}
}
