package com.library.librarymanager.controller;

import com.library.librarymanager.entity.NhaXuatBan;
import com.library.librarymanager.service.NhaXuatBanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nha-xuat-ban")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class NhaXuatBanController {
    private final NhaXuatBanService nhaXuatBanService;
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping
    List<NhaXuatBan> getAll(){return nhaXuatBanService.getAll();}
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/{id}")
    NhaXuatBan getById(@PathVariable int id){return nhaXuatBanService.getById(id);}
    @PostMapping
    NhaXuatBan create(@RequestBody NhaXuatBan nhaXuatBan){return  nhaXuatBanService.create(nhaXuatBan);}
    @PutMapping("/{id}")
    NhaXuatBan updateById(@PathVariable int id, @RequestBody NhaXuatBan nhaXuatBan){return nhaXuatBanService.updateById(id,nhaXuatBan);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){nhaXuatBanService.deleteById(id);}
}
