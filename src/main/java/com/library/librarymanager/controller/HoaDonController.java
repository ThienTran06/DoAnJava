package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.HoaDonRequest;
import com.library.librarymanager.dto.response.ThongKeHoaDon;
import com.library.librarymanager.entity.HoaDon;
import com.library.librarymanager.service.Interface.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('QUAN_LY_HOA_DON')")
public class HoaDonController {
    private final HoaDonService hoaDonService;
    @GetMapping
    public ResponseEntity<Page<HoaDon>> getAll(
            @RequestParam(required = false) Integer ma,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate ngay,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                hoaDonService.getAll(ma, ngay, page, size)
        );
    }
    @GetMapping("/{id}")
    HoaDon getById(@PathVariable int id){return hoaDonService.getById(id);}
    @PostMapping
    HoaDon create(@RequestBody HoaDonRequest hoaDon){return  hoaDonService.create(hoaDon);}
    @PutMapping("/{id}")
    HoaDon updateById(@PathVariable int id, @RequestBody HoaDon hoaDon){return hoaDonService.updateById(id,hoaDon);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){hoaDonService.deleteById(id);}
    @PutMapping("/{id}/huy_don")
    void huyHoaDon(@PathVariable int id) {
        hoaDonService.huyHoaDon(id);
    }
    @GetMapping("/thong_ke")
    public ResponseEntity<ThongKeHoaDon> dashboard() {
        return ResponseEntity.ok(hoaDonService.getThongKe());
    }


    }

