package com.library.librarymanager.service.Interface;

import com.library.librarymanager.entity.DanhGia;
import java.util.List;

public interface DanhGiaService {
    List<DanhGia> getAll();
    DanhGia reply(int id, String reply);
    DanhGia reply(int id, String reply, String adminName);
    DanhGia duyetDanhGia(int id);
    void xoaDanhGia(int id);
    DanhGia taoDanhGia(int khachHangId, Integer sachId, int diemSao, String noiDung, String loai);
    DanhGia submitPublicReview(String hoTen, String sdt, String email, Integer sachId, int diemSao, String noiDung, String loai);
    List<DanhGia> getPublicReviews(); // Trả về đánh giá đã duyệt công cộng
}
