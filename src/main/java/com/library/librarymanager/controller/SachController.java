package com.library.librarymanager.controller;

import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.service.Interface.SachService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sach")
@RequiredArgsConstructor
public class SachController {
    private final SachService sachService;
    @PreAuthorize("hasAuthority('QUAN_LY_SACH')")
    @GetMapping
    List<Sach> getAll(){return sachService.getAll();}
    @PreAuthorize("hasAuthority('QUAN_LY_SACH')")
    @GetMapping("/{id}")
    Sach getById(@PathVariable int id){return sachService.getById(id);}
    @PreAuthorize("hasAuthority('QUAN_LY_SACH')")
    @GetMapping("/search")
    List<Sach> search(
            @RequestParam(required = false) String tenSach,
            @RequestParam(required = false) String tenTheLoai,
            @RequestParam(required = false) String tenTacGia,
            @RequestParam(required = false) Integer namXuatBan) {
        return sachService.search(tenSach, tenTheLoai, tenTacGia, namXuatBan);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    Sach create(@RequestBody Sach sach){return  sachService.create(sach);}
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    Sach updateById(@PathVariable int id, @RequestBody Sach sach){return sachService.updateById(id,sach);}
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){sachService.deleteById(id);}
}

