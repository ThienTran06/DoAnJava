package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.DanhGia;
import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.repository.ChiTietHoaDonRepository;
import com.library.librarymanager.repository.DanhGiaRepository;
import com.library.librarymanager.repository.KhachHangRepository;
import com.library.librarymanager.repository.SachRepository;
import com.library.librarymanager.service.Interface.DanhGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DanhGiaServiceImpl implements DanhGiaService {

    private final DanhGiaRepository danhGiaRepository;
    private final KhachHangRepository khachHangRepository;
    private final SachRepository sachRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;

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
        // Tìm theo id, set reply, set trangThai = "DA_DUYET", save
        DanhGia danhGia = danhGiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay danh gia co id = " + id));
        danhGia.setReply(reply);
        danhGia.setTrangThai("DA_DUYET");
        danhGia.setReplyAt(LocalDateTime.now());
        danhGia.setReplyBy(adminName);
        return danhGiaRepository.save(danhGia);
    }

    @Override
    @Transactional
    public DanhGia duyetDanhGia(int id) {
        // Set trangThai = "DA_DUYET", save
        DanhGia danhGia = danhGiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay danh gia co id = " + id));
        danhGia.setTrangThai("DA_DUYET");
        return danhGiaRepository.save(danhGia);
    }

    @Override
    @Transactional
    public void xoaDanhGia(int id) {
        // Set trangThai = "DA_XOA", save (xóa mềm)
        DanhGia danhGia = danhGiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay danh gia co id = " + id));
        danhGia.setTrangThai("DA_XOA");
        danhGiaRepository.save(danhGia);
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
        danhGia.setTrangThai("CHO_DUYET");
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

        // Validate theo loại đánh giá
        if ("SACH".equalsIgnoreCase(loai)) {
            // Đánh giá sách: phải đã mua sách đó
            if (sachId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đánh giá sách phải có sachId");
            }
            // Check xem khách hàng đã mua sách này chưa
            if (!chiTietHoaDonRepository.hasCustomerBoughtBook(khachHang.getId(), sachId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn phải đã mua sách này mới có thể đánh giá");
            }
        } else if ("DICH_VU".equalsIgnoreCase(loai)) {
            // Đánh giá dịch vụ: phải là khách hàng thực (có ít nhất 1 đơn hàng)
            if (!chiTietHoaDonRepository.hasCustomerAnyOrder(khachHang.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn phải là khách hàng của cửa hàng mới có thể đánh giá dịch vụ");
            }
        }

        // Tìm sách (optional)
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
        danhGia.setTrangThai("CHO_DUYET");
        danhGia.setCreatedAt(LocalDateTime.now());

        return danhGiaRepository.save(danhGia);
    }

    @Override
    public List<DanhGia> getPublicReviews() {
        // Trả về đánh giá đã duyệt (public)
        return danhGiaRepository.findByTrangThai("DA_DUYET");
    }
}
