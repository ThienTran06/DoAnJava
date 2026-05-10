package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.PhieuNhapRequest;
import com.library.librarymanager.entity.PhieuNhap;
import com.library.librarymanager.service.PhieuNhapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phieu-nhap")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
public class PhieuNhapController {
    private final PhieuNhapService phieuNhapService;
    @GetMapping
    List<PhieuNhap> getAll(){return phieuNhapService.getAll();}
    @GetMapping("/{id}")
    PhieuNhap getById(@PathVariable int id){return phieuNhapService.getById(id);}
    @PostMapping
    PhieuNhap create(@RequestBody PhieuNhapRequest request){return  phieuNhapService.create(request);}
    @PutMapping("/{id}")
    PhieuNhap updateById(@PathVariable int id, @RequestBody PhieuNhap phieuNhap){return phieuNhapService.updateById(id,phieuNhap);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){phieuNhapService.deleteById(id);}
}
