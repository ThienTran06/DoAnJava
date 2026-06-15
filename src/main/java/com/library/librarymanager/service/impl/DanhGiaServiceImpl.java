package com.library.librarymanager.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.library.librarymanager.entity.DanhGia;
import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.repository.ChiTietHoaDonRepository;
import com.library.librarymanager.repository.DanhGiaRepository;
import com.library.librarymanager.repository.KhachHangRepository;
import com.library.librarymanager.repository.SachRepository;
import com.library.librarymanager.service.Interface.CloudinaryService;
import com.library.librarymanager.service.Interface.DanhGiaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DanhGiaServiceImpl implements DanhGiaService {

    private final DanhGiaRepository danhGiaRepository;
    private final KhachHangRepository khachHangRepository;
    private final SachRepository sachRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public List<DanhGia> getAll() {
        // Trả về tất cả đánh giá không có trangThai = "DA_XOA"
        return danhGiaRepository.findByTrangThaiNot("DA_XOA");
    }

    @Override
    @Transactional
    public DanhGia reply(int id, String reply) {
        // Gọi phiên bản mới với adminName = null
        return this.reply(id, reply, null);
    }

    @Override
    @Transactional
    public DanhGia reply(int id, String reply, String adminName) {
        DanhGia danhGia = danhGiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay danh gia co id = " + id));
        danhGia.setReply(reply);
        danhGia.setReplyAt(LocalDateTime.now());
        danhGia.setReplyBy(adminName);
        return danhGiaRepository.save(danhGia);
    }

    @Override
    @Transactional
    public void xoaDanhGia(int id) {
        if (!danhGiaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay danh gia co id = " + id);
        }
        danhGiaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public DanhGia taoDanhGia(int khachHangId, Integer sachId, int diemSao, String noiDung, String loai) {
        // Tìm khách hàng
        KhachHang khachHang = khachHangRepository.findById(khachHangId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay khach hang co id = " + khachHangId));

        // Tìm sách (optional)
        Sach sach = null;
        if (sachId != null) {
            sach = sachRepository.findById(sachId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay sach co id = " + sachId));
        }

        // Tạo mới DanhGia
        DanhGia danhGia = new DanhGia();
        danhGia.setKhachHang(khachHang);
        danhGia.setSach(sach);
        danhGia.setDiemSao(diemSao);
        danhGia.setNoiDung(noiDung);
        danhGia.setLoai(loai);
        danhGia.setTrangThai("DA_DUYET");
        danhGia.setCreatedAt(LocalDateTime.now());

        return danhGiaRepository.save(danhGia);
    }

    @Override
    @Transactional
    public DanhGia submitPublicReview(String hoTen, String sdt, String email, Integer sachId, int diemSao, String noiDung, String loai) {
        // Tìm hoặc tạo khách hàng dựa trên số điện thoại
        KhachHang khachHang = khachHangRepository.findBySDT(sdt)
                .orElseGet(() -> {
                    KhachHang newCustomer = new KhachHang();
                    newCustomer.setHoTen(hoTen);
                    newCustomer.setSDT(sdt);
                    newCustomer.setEmail(email);
                    newCustomer.setHangThanhVien("Đồng");
                    newCustomer.setVip(false);
                    newCustomer.setDiemTichLuy(0);
                    return khachHangRepository.save(newCustomer);
                });
        // Tim sach (optional)
        Sach sach = null;
        if (sachId != null) {
            sach = sachRepository.findById(sachId)
                    .orElse(null);
        }

        // Tạo đánh giá mới
        DanhGia danhGia = new DanhGia();
        danhGia.setKhachHang(khachHang);
        danhGia.setSach(sach);
        danhGia.setDiemSao(diemSao);
        danhGia.setNoiDung(noiDung);
        danhGia.setLoai(loai);
        danhGia.setTrangThai("DA_DUYET");
        danhGia.setCreatedAt(LocalDateTime.now());

        return danhGiaRepository.save(danhGia);
    }

    @Override
    @Transactional
    public DanhGia submitPublicReview(String hoTen, String sdt, String email, Integer sachId,
            int diemSao, String noiDung, String loai, List<MultipartFile> hinhAnh) {
        DanhGia dg = submitPublicReview(hoTen, sdt, email, sachId, diemSao, noiDung, loai);
        if (hinhAnh != null && !hinhAnh.isEmpty()) {
            String urls = hinhAnh.stream()
                .filter(f -> f != null && !f.isEmpty())
                .limit(3)
                .map(this::uploadReviewImage)
                .collect(java.util.stream.Collectors.joining(","));
            dg.setHinhAnh(urls);
            danhGiaRepository.save(dg);
        }
        return dg;
    }

    private String uploadReviewImage(MultipartFile file) {
        try {
            return cloudinaryService.uploadFile(file);
        } catch (RuntimeException ex) {
            return "";
        }
    }

    @Override
    public List<DanhGia> getPublicReviews() {
        // Trả về đánh giá đã duyệt (public)
        return danhGiaRepository.findByTrangThaiNot("DA_XOA");
    }
}
