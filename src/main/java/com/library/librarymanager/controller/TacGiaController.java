package com.library.librarymanager.controller;

import com.library.librarymanager.entity.TacGia;
import com.library.librarymanager.service.TacGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tac-gia")
@RequiredArgsConstructor
public class TacGiaController {
    private final TacGiaService tacGiaService;
    @GetMapping
    List<TacGia> getAll(){return tacGiaService.getAll();}
    @GetMapping("/{id}")
    TacGia getById(@PathVariable int id){return tacGiaService.getById(id);}
    @PostMapping
    TacGia create(@RequestBody TacGia tacGia){return  tacGiaService.create(tacGia);}
    @PutMapping("/{id}")
    TacGia updateById(@PathVariable int id, @RequestBody TacGia tacGia){return tacGiaService.updateById(id,tacGia);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){tacGiaService.deleteById(id);}
}
