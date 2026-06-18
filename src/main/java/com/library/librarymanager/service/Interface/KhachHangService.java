package com.library.librarymanager.service.Interface;

import com.library.librarymanager.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface KhachHangService {
    List<KhachHang> getAll();
    KhachHang getById(int id);
    KhachHang login(String email, String password);
    KhachHang create(KhachHang khachHang);
    KhachHang create(KhachHang khachHang, MultipartFile avatarFile);
    KhachHang updateById(int id,KhachHang khachHang);
    KhachHang updateById(int id, KhachHang khachHang, MultipartFile avatarFile);
    void deleteById(int id);
    BigDecimal tinhTongTienSauGiamGia(KhachHang khachHang, BigDecimal tongTien);
    void congDiemTuHoaDon(KhachHang khachHang, BigDecimal tongTienSauGiamGia);
    void capNhatHangThanhVien(KhachHang khachHang);
    String uploadAvatar(MultipartFile avatarFile);
    Page<KhachHang> getDanhSachKhachHang(
            String keyword,
            int page,
            int size
    );
}
