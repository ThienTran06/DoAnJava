package com.library.librarymanager.controller;

import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.service.Interface.SachService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sach")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('QUAN_LY_SACH')")
public class SachController {

    private final SachService sachService;

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
            @RequestParam(required = false) MultipartFile hinhAnh
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
    //Get sách cho giỏ hàng
    @GetMapping("/page")
    public ResponseEntity<Page<Sach>> getDanhSachSach(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                sachService.getDanhSachSach(
                        keyword,
                        page,
                        size
                )
        );
    }
    @GetMapping("/get_all")
    public ResponseEntity<Page<Sach>> getTatCaSach(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                sachService.getTatCaSach(
                        keyword,
                        page,
                        size
                )
        );
    }
}