package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.CreateUserRequest;
import com.library.librarymanager.entity.NguoiDung;
import com.library.librarymanager.service.impl.NguoiDungServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/nguoi-dung")
public class NguoiDungController {

    @Autowired
    private NguoiDungServiceImpl sv;

    @PostMapping("/create")
    public NguoiDung create(@Valid @RequestBody CreateUserRequest req) {

        return sv.create(req);
    }

    @GetMapping("/{id}")
    public NguoiDung findById(@PathVariable int id) {

        return sv.getById(id);
    }

    @GetMapping
    public List<NguoiDung> findAll() {

        return sv.getAll();
    }

    @PutMapping("/{id}")
    public NguoiDung updateById(
            @Valid @RequestBody CreateUserRequest req,
            @PathVariable int id
    ) {

        return sv.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id) {

        sv.delete(id);
    }

    @PutMapping("/{id}/grant-all")
    public void grantedAllPermissions(@PathVariable int id) {

        sv.addAllPermissions(id);
    }
}