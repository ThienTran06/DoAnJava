package com.library.librarymanager.controller;

import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.service.Interface.CloudinaryService;
import com.library.librarymanager.service.Interface.SachService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/sach")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('QUAN_LY_SACH')")
public class SachController {

    private final SachService sachService;
    private final CloudinaryService cloudinaryService;

    @GetMapping("/page")
    Page<Sach> getPage(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return sachService.getDanhSachSach(keyword, page, size);
    }

    @GetMapping("/page-all")
    Page<Sach> getPageAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return sachService.getTatCaSach(keyword, page, size);
    }

    @GetMapping
    List<Sach> getAll() {
        return sachService.getAll();
    }

    @GetMapping("/{id}")
    Sach getById(@PathVariable int id) {
        return sachService.getById(id);
    }

    @GetMapping("/search")
    List<Sach> search(
            @RequestParam(required = false) String tenSach,
            @RequestParam(required = false) String tenTheLoai,
            @RequestParam(required = false) String tenTacGia,
            @RequestParam(required = false) Integer namXuatBan
    ) {

        return sachService.search(
                tenSach,
                tenTheLoai,
                tenTacGia,
                namXuatBan
        );
    }

    @PostMapping
    Sach create(
            @RequestParam String tenSach,
            @RequestParam BigDecimal giaBan,
            @RequestParam Integer soLuongTon,
            @RequestParam Integer namXuatBan,
            @RequestParam Integer theLoaiId,
            @RequestParam Integer nhaXuatBanId,
            @RequestParam List<Integer> tacGiaIds,
            @RequestParam MultipartFile hinhAnh
    ) {

        return sachService.create(
                tenSach,
                giaBan,
                soLuongTon,
                namXuatBan,
                theLoaiId,
                nhaXuatBanId,
                tacGiaIds,
                hinhAnh
        );
    }

    @PutMapping("/{id}")
    Sach updateById(
            @PathVariable int id,
            @RequestParam String tenSach,
            @RequestParam BigDecimal giaBan,
            @RequestParam Integer soLuongTon,
            @RequestParam Integer namXuatBan,
            @RequestParam Integer theLoaiId,
            @RequestParam Integer nhaXuatBanId,
            @RequestParam List<Integer> tacGiaIds,
            @RequestParam(required = false) MultipartFile hinhAnh
    ) {

        return sachService.updateById(
                id,
                tenSach,
                giaBan,
                soLuongTon,
                namXuatBan,
                theLoaiId,
                nhaXuatBanId,
                tacGiaIds,
                hinhAnh
        );
    }

    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id) {
        sachService.deleteById(id);
    }
}