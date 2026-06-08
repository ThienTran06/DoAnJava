package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.CreateUserRequest;
import com.library.librarymanager.dto.request.UpdatePermissionsRequest;
import com.library.librarymanager.dto.response.NhanVienXuatSacResponse;
import com.library.librarymanager.entity.NguoiDung;
import com.library.librarymanager.service.Interface.NguoiDungService;
import com.library.librarymanager.service.impl.NguoiDungServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@PreAuthorize("hasAnyAuthority('QUAN_LY_NGUOI_DUNG','ADMIN')")
@RestController
@RequestMapping("/api/nguoi-dung")
public class NguoiDungController {
    @Autowired
    private NguoiDungService sv;
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

    @GetMapping("/xuat-sac")
    public List<NhanVienXuatSacResponse> getNhanVienXuatSac(
            @RequestParam(defaultValue = "5") int limit
    ) {

        return sv.getNhanVienXuatSac(limit);
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
    @PutMapping("/{id}/permissions")
    public void updatePermissions(@PathVariable int id, @Valid @RequestBody UpdatePermissionsRequest req ){
        sv.updatePermissions(id,req.getPermissionIds());
    }

}
