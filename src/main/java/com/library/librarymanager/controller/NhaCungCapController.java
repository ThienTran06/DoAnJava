package com.library.librarymanager.controller;

import com.library.librarymanager.entity.NhaCungCap;
import com.library.librarymanager.service.Interface.NhaCungCapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nha-cung-cap")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('QUAN_LY_NHA_CUNG_CAP')")
public class NhaCungCapController {
    private final NhaCungCapService nhaCungCapService;

    @GetMapping
    List<NhaCungCap> getAll(){return nhaCungCapService.getAll();}

    @GetMapping("/{id}")
    NhaCungCap getById(@PathVariable int id){return nhaCungCapService.getById(id);}
    @PostMapping
    NhaCungCap create(@RequestBody NhaCungCap nhaCungCap){return  nhaCungCapService.create(nhaCungCap);}
    @PutMapping("/{id}")
    NhaCungCap updateById(@PathVariable int id, @RequestBody NhaCungCap nhaCungCap){return nhaCungCapService.updateById(id,nhaCungCap);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){nhaCungCapService.deleteById(id);}
}
