package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.*;
import com.library.librarymanager.enums.TrangThaiGiu;
import com.library.librarymanager.repository.*;
import com.library.librarymanager.service.Interface.KhachHangService;
import com.library.librarymanager.service.Interface.PhieuGiuSachService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PhieuGiuSachServiceImpl implements PhieuGiuSachService {

    private final ChiTietPhieuGiuRepository ctRepo;
    private final PhieuDatGiuSachRepository phieuRepo;
    private final HoaDonRepository hoaDonRepo;
    private final ChiTietHoaDonRepository cthdRepo;
    private final KhachHangRepository khachHangRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final SachRepository sachRepository;
    private final KhachHangService khachHangService;

    public PhieuGiuSachServiceImpl(
            ChiTietPhieuGiuRepository ctRepo,
            PhieuDatGiuSachRepository phieuRepo,
            HoaDonRepository hoaDonRepo,
            ChiTietHoaDonRepository cthdRepo,
            KhachHangRepository khachHangRepository,
            NguoiDungRepository nguoiDungRepository,
            SachRepository sachRepository,
            KhachHangService khachHangService
    ) {
        this.ctRepo = ctRepo;
        this.phieuRepo = phieuRepo;
        this.hoaDonRepo = hoaDonRepo;
        this.cthdRepo = cthdRepo;
        this.khachHangRepository = khachHangRepository;
        this.nguoiDungRepository = nguoiDungRepository;
        this.sachRepository = sachRepository;
        this.khachHangService = khachHangService;
    }

    @Override
    @Transactional
    public int taoPhieu(int khachHangId) {

        if (phieuRepo.existsByKhachHangIdAndTrangThai(
                khachHangId,
                TrangThaiGiu.PENDING
        )) {
            throw new RuntimeException("Da ton tai phieu pending");
        }

        PhieuDatGiuSach p = new PhieuDatGiuSach();

        p.setKhachHangId(khachHangId);
        p.setCreatedAt(LocalDateTime.now());
        p.setExpiredAt(LocalDateTime.now().plusDays(2));
        p.setTrangThai(TrangThaiGiu.PENDING);

        return phieuRepo.save(p).getId();
    }

    @Override
    @Transactional
    public HoaDon confirm(int phieuId, int nhanVienId) {

        PhieuDatGiuSach p = phieuRepo.findByIdForUpdate(phieuId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Khong tim thay phieu giu sach co id = " + phieuId
                ));

        if (p.getTrangThai() != TrangThaiGiu.PENDING) {
            throw new RuntimeException("Phieu khong hop le");
        }

        if (p.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Phieu da het han");
        }

        List<ChiTietPhieuGiu> ds = ctRepo.findByPhieuGiuId(phieuId);

        if (ds == null || ds.isEmpty()) {
            throw new RuntimeException("Phieu rong");
        }

        KhachHang kh = khachHangRepository.findById(p.getKhachHangId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay khach hang"));

        NguoiDung nv = nguoiDungRepository.findById(nhanVienId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay nhan vien"));

        HoaDon hd = new HoaDon();
        hd.setKhachHang(kh);
        hd.setNhanVien(nv);
        hd.setNgayBan(LocalDateTime.now());
        hd.setTrangThai("PENDING");

        hoaDonRepo.save(hd);

        BigDecimal tongTien = BigDecimal.ZERO;
        List<ChiTietHoaDon> dsct = new ArrayList<>();

        for (ChiTietPhieuGiu ct : ds) {

            Sach s = sachRepository.findByIdForUpdate(ct.getSach().getId())
                    .orElseThrow(() -> new RuntimeException("Khong tim thay sach"));

            ChiTietHoaDon cthd = new ChiTietHoaDon();
            cthd.setHoaDon(hd);
            cthd.setSach(s);
            cthd.setSoLuong(ct.getSoLuong());
            cthd.setDonGia(s.getGiaBan());
            cthd.setTenSach(s.getTenSach());
            cthd.setHinhAnh(s.getHinhAnh());

            BigDecimal thanhTien = s.getGiaBan()
                    .multiply(BigDecimal.valueOf(ct.getSoLuong()));

            cthd.setThanhTien(thanhTien);

            cthdRepo.save(cthd);

            dsct.add(cthd);
            tongTien = tongTien.add(thanhTien);
        }

        hd.setDanhSachChiTiet(dsct);

        khachHangService.capNhatHangThanhVien(kh);

        BigDecimal tongTienSauGiamGia =
                khachHangService.tinhTongTienSauGiamGia(kh, tongTien);

        hd.setTongTien(tongTienSauGiamGia);

        hoaDonRepo.save(hd);

        khachHangService.congDiemTuHoaDon(kh, tongTienSauGiamGia);

        p.setTrangThai(TrangThaiGiu.CONFIRMED);
        phieuRepo.save(p);

        return hd;
    }
    @Override
    @Transactional
    public void expire(int phieuId) {

        PhieuDatGiuSach p = phieuRepo.findByIdForUpdate(phieuId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay phieu"));
        if(p.getTrangThai()==TrangThaiGiu.PENDING){
            List<ChiTietPhieuGiu> ds = ctRepo.findByPhieuGiuId(phieuId);

            for (ChiTietPhieuGiu ct : ds) {

                Sach s = sachRepository.findByIdForUpdate(ct.getSach().getId()).orElseThrow(() -> new RuntimeException("Khong tim thay sach"));

                s.setSoLuongTon(
                        s.getSoLuongTon() + ct.getSoLuong()
                );
            }

            p.setTrangThai(TrangThaiGiu.EXPIRED);

            phieuRepo.save(p);
        }
        else throw new RuntimeException("Không thể expire phiếu giữ không có trạng thái pending");
    }

    @Override
    @Transactional
    public void huy(int phieuId) {

        expire(phieuId);

        PhieuDatGiuSach p = phieuRepo.findByIdForUpdate(phieuId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay phieu"));

        p.setTrangThai(TrangThaiGiu.CANCELLED);

        phieuRepo.save(p);
    }
    @Override
    public Page<PhieuDatGiuSach> getAll(Integer ma, LocalDate ngay, int page, int size) {

        LocalDateTime tuNgay = null;
        LocalDateTime denNgay = null;

        if (ngay != null) {
            tuNgay = ngay.atStartOfDay();
            denNgay = ngay.plusDays(1).atStartOfDay();
        }

        return phieuRepo.getAll(
                ma,
                tuNgay,
                denNgay,
                PageRequest.of(page, size)
        );
    }
    @Override
    public PhieuDatGiuSach getById(int id){
        return phieuRepo.getById(id);
    }
}