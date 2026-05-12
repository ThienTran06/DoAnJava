package com.library.librarymanager.controller;

import com.library.librarymanager.entity.TacGia;
import com.library.librarymanager.service.Interface.TacGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tac-gia")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TacGiaController {
    private final TacGiaService tacGiaService;
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping
    List<TacGia> getAll(){return tacGiaService.getAll();}
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/{id}")
    TacGia getById(@PathVariable int id){return tacGiaService.getById(id);}
    @PostMapping
    TacGia create(@RequestBody TacGia tacGia){return  tacGiaService.create(tacGia);}
    @PutMapping("/{id}")
    TacGia updateById(@PathVariable int id, @RequestBody TacGia tacGia){return tacGiaService.updateById(id,tacGia);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){tacGiaService.deleteById(id);}
}
