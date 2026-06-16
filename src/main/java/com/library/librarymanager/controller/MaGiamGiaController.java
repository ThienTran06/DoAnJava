package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.DoiDiemMaGiamGiaRequest;
import com.library.librarymanager.entity.MaGiamGia;
import com.library.librarymanager.service.Interface.MaGiamGiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ma-giam-gia")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('QUAN_LY_KHACH_HANG')")
public class MaGiamGiaController {
    private final MaGiamGiaService maGiamGiaService;

    @GetMapping
    List<MaGiamGia> getAll() {
        return maGiamGiaService.getAll();
    }

    @PostMapping("/khach-hang/{khachHangId}/doi-diem")
    MaGiamGia doiDiem(
            @PathVariable int khachHangId,
            @Valid @RequestBody DoiDiemMaGiamGiaRequest request
    ) {
        return maGiamGiaService.doiDiem(khachHangId, request.getDiemSuDung());
    }

    @GetMapping("/khach-hang/{khachHangId}")
    List<MaGiamGia> getByKhachHangId(@PathVariable int khachHangId) {
        return maGiamGiaService.getByKhachHangId(khachHangId);
    }

    @DeleteMapping("/{id}")
    void deleteUsedById(@PathVariable int id) {
        maGiamGiaService.deleteUsedById(id);
    }
}
