package com.library.librarymanager.controller;

import com.library.librarymanager.entity.NhanVien;
import com.library.librarymanager.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nhan-vien")
@RequiredArgsConstructor
public class NhanVienController {
    private final NhanVienService nhanVienService;
    @GetMapping
    List<NhanVien> getAll(){return nhanVienService.getAll();}
    @GetMapping("/{id}")
    NhanVien getById(@PathVariable int id){return nhanVienService.getById(id);}
    @PostMapping
    NhanVien create(@RequestBody NhanVien nhanVien){return  nhanVienService.create(nhanVien);}
    @PutMapping("/{id}")
    NhanVien updateById(@PathVariable int id, @RequestBody NhanVien nhanVien){return nhanVienService.updateById(id,nhanVien);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){nhanVienService.deleteById(id);}
}

