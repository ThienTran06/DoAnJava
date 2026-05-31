package com.library.librarymanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.library.librarymanager.dto.request.CreateUserRequest;
import com.library.librarymanager.dto.request.UpdatePermissionsRequest;
import com.library.librarymanager.entity.NguoiDung;
import com.library.librarymanager.service.Interface.NguoiDungService;

@PreAuthorize("hasAuthority('QUAN_LY_NGUOI_DUNG')")
@RestController
@RequestMapping("/api/nguoi-dung")
public class NguoiDungController {
    @Autowired
    private NguoiDungService sv;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public NguoiDung create(
            @RequestParam String tenDangNhap,
            @RequestParam String matKhau,
            @RequestParam String hoTen,
            @RequestParam String sdt,
            @RequestParam String tenNhom,
            @RequestParam List<Integer> permissionIds,
            @RequestParam(required = false) String chucVu,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String caLamViec,
            @RequestParam(required = false) String diaChi,
            @RequestParam(required = false) String ngayVaoLam,
            @RequestParam(required = false) Long luongCoBan,
            @RequestParam(required = false) String ghiChu,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ) {
        CreateUserRequest req = new CreateUserRequest();
        req.setTenDangNhap(tenDangNhap);
        req.setMatKhau(matKhau);
        req.setHoTen(hoTen);
        req.setSdt(sdt);
        req.setTenNhom(tenNhom);
        req.setChucVu(chucVu);
        req.setPermissionIds(permissionIds);
        req.setEmail(email);
        req.setCaLamViec(caLamViec);
        req.setDiaChi(diaChi);
        req.setNgayVaoLam(ngayVaoLam);
        req.setLuongCoBan(luongCoBan);
        req.setGhiChu(ghiChu);
        req.setAvatarFile(avatar);
        return sv.create(req);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public NguoiDung updateById(
        @PathVariable int id,
        @RequestParam String tenDangNhap,
        @RequestParam String matKhau,
        @RequestParam String hoTen,
        @RequestParam String sdt,
        @RequestParam String tenNhom,
        @RequestParam List<Integer> permissionIds,
        @RequestParam(required = false) String chucVu,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String caLamViec,
        @RequestParam(required = false) String diaChi,
        @RequestParam(required = false) String ngayVaoLam,
        @RequestParam(required = false) Long luongCoBan,
        @RequestParam(required = false) String ghiChu,
        @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ) {
        CreateUserRequest req = new CreateUserRequest();
        req.setTenDangNhap(tenDangNhap);
        req.setMatKhau(matKhau);
        req.setHoTen(hoTen);
        req.setSdt(sdt);
        req.setTenNhom(tenNhom);
        req.setPermissionIds(permissionIds);
        req.setChucVu(chucVu);
        req.setEmail(email);
        req.setCaLamViec(caLamViec);
        req.setDiaChi(diaChi);
        req.setNgayVaoLam(ngayVaoLam);
        req.setLuongCoBan(luongCoBan);
        req.setGhiChu(ghiChu);
        req.setAvatarFile(avatar);
        return sv.update(id, req);
    }
    @GetMapping("/{id}")
    public NguoiDung findById(@PathVariable int id) {
        return sv.getById(id);
    }

    @GetMapping
    public List<NguoiDung> findAll() {
        return sv.getAll();
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
    public void updatePermissions(@PathVariable int id, @RequestBody UpdatePermissionsRequest req) {
        sv.updatePermissions(id, req.getPermissionIds());
    }
}