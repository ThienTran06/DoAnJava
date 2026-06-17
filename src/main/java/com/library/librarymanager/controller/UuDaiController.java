package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.UuDaiRequest;
import com.library.librarymanager.entity.UuDai;
import com.library.librarymanager.service.Interface.UuDaiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/uu-dai")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('QUAN_LY_SACH')")
public class UuDaiController {
    private final UuDaiService uuDaiService;

    @GetMapping
    List<UuDai> getAll() {
        return uuDaiService.getAll();
    }

    @PostMapping
    UuDai create(@Valid @RequestBody UuDaiRequest request) {
        return uuDaiService.create(request);
    }

    @PutMapping("/{id}")
    UuDai update(@PathVariable int id, @Valid @RequestBody UuDaiRequest request) {
        return uuDaiService.update(id, request);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable int id) {
        uuDaiService.delete(id);
    }
}
