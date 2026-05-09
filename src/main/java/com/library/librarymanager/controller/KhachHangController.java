package com.library.librarymanager.controller;

import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khach-hang")
@RequiredArgsConstructor
public class KhachHangController {
    private final KhachHangService khachHangService;
    @GetMapping
    List<KhachHang> getAll(){return khachHangService.getAll();}
    @GetMapping("/{id}")
    KhachHang getById(@PathVariable int id){return khachHangService.getById(id);}
    @PostMapping
    KhachHang create(@RequestBody KhachHang khachHang){return  khachHangService.create(khachHang);}
    @PutMapping("/{id}")
    KhachHang updateById(@PathVariable int id, @RequestBody KhachHang khachHang){return khachHangService.updateById(id,khachHang);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){khachHangService.deleteById(id);}
}
