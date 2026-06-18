package com.library.librarymanager.service.Interface;

import com.library.librarymanager.entity.DanhGia;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface DanhGiaService {
    List<DanhGia> getAll();
    DanhGia reply(int id, String reply);
    DanhGia reply(int id, String reply, String adminName);
    void xoaDanhGia(int id);
    DanhGia taoDanhGia(int khachHangId, Integer sachId, int diemSao, String noiDung, String loai);
    DanhGia submitPublicReview(Integer khachHangId, String hoTen, String sdt, String email, Integer sachId, int diemSao, String noiDung, String loai);
    DanhGia submitPublicReview(Integer khachHangId, String hoTen, String sdt, String email, Integer sachId, int diemSao, String noiDung, String loai, List<MultipartFile> hinhAnh);
    List<DanhGia> getPublicReviews(); // Trả về đánh giá đã duyệt công cộng
}
