package com.library.librarymanager.controller;

import com.library.librarymanager.entity.TheLoai;
import com.library.librarymanager.service.Interface.TheLoaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/the-loai")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('QUAN_LY_THE_LOAI')")
public class TheLoaiController {
    private final TheLoaiService theLoaiService;

    @GetMapping
    List<TheLoai> getAll(){return theLoaiService.getAll();}

    @GetMapping("/{id}")
    TheLoai getById(@PathVariable int id){return theLoaiService.getById(id);}
    @PostMapping
    TheLoai create(@RequestBody TheLoai theLoai){return  theLoaiService.create(theLoai);}
    @PutMapping("/{id}")
    TheLoai updateById(@PathVariable int id, @RequestBody TheLoai theLoai){return theLoaiService.updateById(id,theLoai);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){theLoaiService.deleteById(id);}
}
