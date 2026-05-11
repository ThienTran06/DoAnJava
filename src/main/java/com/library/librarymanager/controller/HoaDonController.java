package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.HoaDonRequest;
import com.library.librarymanager.entity.HoaDon;
import com.library.librarymanager.service.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('QUAN_LY_HOA_DON')")
public class HoaDonController {
    private final HoaDonService hoaDonService;
    @GetMapping
    List<HoaDon> getAll(){return hoaDonService.getAll();}
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
}
