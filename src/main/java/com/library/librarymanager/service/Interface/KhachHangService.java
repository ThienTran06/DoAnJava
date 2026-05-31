package com.library.librarymanager.service.Interface;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.library.librarymanager.entity.KhachHang;

public interface KhachHangService {
    List<KhachHang> getAll();
    KhachHang getById(int id);
    KhachHang create(String hoTen, String sdt, String email, String ngaySinh, Integer diemTichLuy, MultipartFile avatar);
    KhachHang updateById(int id, String hoTen, String sdt, String email, String ngaySinh, Integer diemTichLuy, Boolean trangThai, MultipartFile avatar);
    void deleteById(int id);
}