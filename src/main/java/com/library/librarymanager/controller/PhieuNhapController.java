package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.PhieuNhapRequest;
import com.library.librarymanager.dto.request.UpdatePhieuNhapRequest;
import com.library.librarymanager.entity.PhieuNhap;
import com.library.librarymanager.service.Interface.PhieuNhapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phieu-nhap")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('QUAN_LY_PHIEU_NHAP')")
public class PhieuNhapController {
    private final PhieuNhapService phieuNhapService;
    @GetMapping
    List<PhieuNhap> getAll(){return phieuNhapService.getAll();}
    @GetMapping("/{id}")
    PhieuNhap getById(@PathVariable int id){return phieuNhapService.getById(id);}
    @PostMapping
    PhieuNhap create(@Valid @RequestBody PhieuNhapRequest request){return  phieuNhapService.create(request);}
    @PutMapping("/{id}")
    PhieuNhap updateById(@PathVariable int id, @Valid @RequestBody UpdatePhieuNhapRequest request){return phieuNhapService.updateById(id,request);}
    @PutMapping("/{id}/chi-tiet")
    PhieuNhap updateChiTiet(@PathVariable int id, @Valid @RequestBody PhieuNhapRequest request){return phieuNhapService.updateChiTiet(id,request);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){phieuNhapService.deleteById(id);}
    @PutMapping("/{id}/huy-phieu-nhap")
    void huyPhieuNhap(@PathVariable int id){
        phieuNhapService.huyPhieuNhap(id);
    }
}
