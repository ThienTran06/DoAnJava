package com.library.librarymanager.controller;

import com.library.librarymanager.entity.ChiTietHoaDon;
import com.library.librarymanager.service.ChiTietHoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chi-tiet-hoa-don")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ChiTietHoaDonController {
    private final ChiTietHoaDonService chiTietHoaDonService;
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping
    List<ChiTietHoaDon> getAll(){return chiTietHoaDonService.getAll();}
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/{id}")
    ChiTietHoaDon getById(@PathVariable int id){return chiTietHoaDonService.getById(id);}
    @PostMapping
    ChiTietHoaDon create(@RequestBody ChiTietHoaDon chiTietHoaDon){return  chiTietHoaDonService.create(chiTietHoaDon);}
    @PutMapping("/{id}")
    ChiTietHoaDon updateById(@PathVariable int id, @RequestBody ChiTietHoaDon chiTietHoaDon){return chiTietHoaDonService.updateById(id,chiTietHoaDon);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){chiTietHoaDonService.deleteById(id);}
}