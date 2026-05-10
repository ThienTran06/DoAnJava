package com.library.librarymanager.controller;

import com.library.librarymanager.entity.NhaCungCap;
import com.library.librarymanager.service.NhaCungCapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nha-cung-cap")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class NhaCungCapController {
    private final NhaCungCapService nhaCungCapService;
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping
    List<NhaCungCap> getAll(){return nhaCungCapService.getAll();}
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/{id}")
    NhaCungCap getById(@PathVariable int id){return nhaCungCapService.getById(id);}
    @PostMapping
    NhaCungCap create(@RequestBody NhaCungCap nhaCungCap){return  nhaCungCapService.create(nhaCungCap);}
    @PutMapping("/{id}")
    NhaCungCap updateById(@PathVariable int id, @RequestBody NhaCungCap nhaCungCap){return nhaCungCapService.updateById(id,nhaCungCap);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){nhaCungCapService.deleteById(id);}
}
