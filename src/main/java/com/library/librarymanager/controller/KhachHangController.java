package com.library.librarymanager.controller;

import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.service.Interface.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    KhachHang create(@RequestBody KhachHang khachHang){return  khachHangService.create(khachHang);}

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    KhachHang createWithAvatar(
            @RequestPart("khachHang") KhachHang khachHang,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile
    ) {
        return khachHangService.create(khachHang, avatarFile);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    KhachHang updateById(@PathVariable int id, @RequestBody KhachHang khachHang){return khachHangService.updateById(id,khachHang);}

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    KhachHang updateByIdWithAvatar(
            @PathVariable int id,
            @RequestPart("khachHang") KhachHang khachHang,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile
    ) {
        return khachHangService.updateById(id, khachHang, avatarFile);
    }

    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){khachHangService.deleteById(id);}

    @PostMapping("/upload-avatar")
    public Map<String, String> uploadAvatar(
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            @RequestParam(value = "fileAnh", required = false) MultipartFile fileAnh
    ) {
        String url = khachHangService.uploadAvatar(avatarFile != null ? avatarFile : fileAnh);
        return Map.of("url", url);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<KhachHang>> getDanhSachKhachHang(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                khachHangService.getDanhSachKhachHang(
                        keyword,
                        page,
                        size
                )
        );
    }
}
