package com.library.librarymanager.controller;

import com.library.librarymanager.dto.response.SachTonKhoResponse;
import com.library.librarymanager.dto.response.SachThongKeResponse;
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
    List<Sach> getAll(){return sachService.getAll();}

    @GetMapping("/{id}")
    Sach getById(@PathVariable int id){return sachService.getById(id);}

    @GetMapping("/search")
    List<Sach> search(
            @RequestParam(required = false) String tenSach,
            @RequestParam(required = false) String tenTheLoai,
            @RequestParam(required = false) String tenTacGia,
            @RequestParam(required = false) Integer namXuatBan) {
        return sachService.search(tenSach, tenTheLoai, tenTacGia, namXuatBan);
    }

    @GetMapping("/thong-ke")
    SachThongKeResponse getThongKeSach() {
        return sachService.getThongKeSach();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    Sach create(@Valid @RequestBody Sach sach){return  sachService.create(sach);}

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Sach createWithImage(
            @Valid @RequestPart("sach") Sach sach,
            @RequestPart(value = "fileAnh", required = false) MultipartFile fileAnh
    ) {
        return sachService.create(sach, fileAnh);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    Sach updateById(@PathVariable int id, @RequestBody Sach sach){return sachService.updateById(id,sach);}

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Sach updateByIdWithImage(
            @PathVariable int id,
            @RequestPart("sach") Sach sach,
            @RequestPart(value = "fileAnh", required = false) MultipartFile fileAnh
    ) {
        return sachService.updateById(id, sach, fileAnh);
    }

    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){sachService.deleteById(id);}
    @GetMapping("/ton-kho")
    List<SachTonKhoResponse>getSachTonKho(@RequestParam String tenSach){
        return sachService.getStockByName(tenSach);
    }
    @PostMapping("/upload-image")
    public Map<String, String> uploadImage(@RequestParam MultipartFile fileAnh) {
        String url = cloudinaryService.uploadFile(fileAnh);
        return Map.of("url", url);
    }
}

