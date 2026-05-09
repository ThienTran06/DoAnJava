package com.library.librarymanager.controller;

import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.service.SachService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sach")
@RequiredArgsConstructor
public class SachController {
    private final SachService sachService;
    @GetMapping
    List<Sach> getAll(){return sachService.getAll();}
    @GetMapping("/{id}")
    Sach getById(@PathVariable int id){return sachService.getById(id);}
    @PostMapping
    Sach create(@RequestBody Sach sach){return  sachService.create(sach);}
    @PutMapping("/{id}")
    Sach updateById(@PathVariable int id, @RequestBody Sach sach){return sachService.updateById(id,sach);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){sachService.deleteById(id);}
}

