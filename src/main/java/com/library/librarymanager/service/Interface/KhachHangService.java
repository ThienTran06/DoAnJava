package com.library.librarymanager.service.Interface;

import com.library.librarymanager.entity.KhachHang;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface KhachHangService {
    List<KhachHang> getAll();
    KhachHang getById(int id);
    KhachHang create(String hoTen, String sdt, String email, org.springframework.web.multipart.MultipartFile avatar);
    KhachHang updateById(int id, String hoTen, String sdt, String email, Integer diemTichLuy, Boolean trangThai, MultipartFile avatar);
    void deleteById(int id);
}
