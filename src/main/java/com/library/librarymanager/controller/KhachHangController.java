package com.library.librarymanager.controller;

import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.service.Interface.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@PreAuthorize("hasAuthority('QUAN_LY_KHACH_HANG')")
@RestController
@RequestMapping("/api/khach-hang")
@RequiredArgsConstructor
public class KhachHangController {
    private final KhachHangService khachHangService;
    @GetMapping
    List<KhachHang> getAll(){return khachHangService.getAll();}
    @GetMapping("/{id}")
    KhachHang getById(@PathVariable int id){return khachHangService.getById(id);}
    @PostMapping(consumes = "multipart/form-data")
    KhachHang create(
            @RequestParam String hoTen,
            @RequestParam String sdt,
            @RequestParam String email,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ){
        return  khachHangService.create(hoTen, sdt, email, avatar);
    }
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    KhachHang updateById(
            @PathVariable int id,
            @RequestParam String hoTen,
            @RequestParam String sdt,
            @RequestParam String email,
            @RequestParam(required = false) Integer diemTichLuy,
            @RequestParam(required = false) Boolean trangThai,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ){
        return khachHangService.updateById(id, hoTen, sdt, email, diemTichLuy, trangThai, avatar);
    }
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){khachHangService.deleteById(id);}
}
