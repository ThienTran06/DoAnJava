package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.entity.MaGiamGia;
import com.library.librarymanager.repository.KhachHangRepository;
import com.library.librarymanager.repository.HoaDonRepository;
import com.library.librarymanager.repository.MaGiamGiaRepository;
import com.library.librarymanager.service.Interface.MaGiamGiaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaGiamGiaServiceImpl implements MaGiamGiaService {
    private static final int DIEM_TOI_THIEU = 100;
    private static final BigDecimal GIA_TRI_MOI_DIEM = BigDecimal.valueOf(100);
    private static final int SO_NGAY_HET_HAN = 30;

    private final MaGiamGiaRepository maGiamGiaRepository;
    private final KhachHangRepository khachHangRepository;
    private final HoaDonRepository hoaDonRepository;

    @Override
    public List<MaGiamGia> getAll() {
        return maGiamGiaRepository.findAllByOrderByNgayTaoDesc();
    }

    @Override
    @Transactional
    public MaGiamGia doiDiem(int khachHangId, int diemSuDung) {
        if (diemSuDung < DIEM_TOI_THIEU) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can doi toi thieu " + DIEM_TOI_THIEU + " diem");
        }

        KhachHang khachHang = khachHangRepository.findByIdForUpdate(khachHangId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay khach hang co id = " + khachHangId));
        if (khachHang.getDiemTichLuy() < diemSuDung) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khach hang khong du diem de doi ma giam gia");
        }

        khachHang.setDiemTichLuy(khachHang.getDiemTichLuy() - diemSuDung);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        MaGiamGia maGiamGia = new MaGiamGia();
        maGiamGia.setMa(taoMaGiamGia());
        maGiamGia.setKhachHang(khachHang);
        maGiamGia.setDiemDaDoi(diemSuDung);
        maGiamGia.setGiaTriGiam(GIA_TRI_MOI_DIEM.multiply(BigDecimal.valueOf(diemSuDung)));
        maGiamGia.setTrangThai("CHUA_SU_DUNG");
        maGiamGia.setNgayTao(now);
        maGiamGia.setNgayHetHan(now.plusDays(SO_NGAY_HET_HAN));

        khachHangRepository.save(khachHang);
        return maGiamGiaRepository.save(maGiamGia);
    }

    @Override
    public List<MaGiamGia> getByKhachHangId(int khachHangId) {
        if (!khachHangRepository.existsById(khachHangId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay khach hang co id = " + khachHangId);
        }
        return maGiamGiaRepository.findByKhachHangIdOrderByNgayTaoDesc(khachHangId);
    }

    @Override
    @Transactional
    public MaGiamGia suDungMa(KhachHang khachHang, String ma) {
        if (ma == null || ma.isBlank()) {
            return null;
        }

        String normalized = ma.trim().toUpperCase(Locale.ROOT);
        MaGiamGia maGiamGia = maGiamGiaRepository.findByMaForUpdate(normalized)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ma giam gia khong ton tai"));

        if (maGiamGia.getKhachHang().getId() != khachHang.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ma giam gia khong thuoc ve khach hang nay");
        }
        if (!"CHUA_SU_DUNG".equals(maGiamGia.getTrangThai())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ma giam gia da duoc su dung hoac khong con hop le");
        }
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        if (maGiamGia.getNgayHetHan() != null && maGiamGia.getNgayHetHan().isBefore(now)) {
            maGiamGia.setTrangThai("HET_HAN");
            maGiamGiaRepository.save(maGiamGia);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ma giam gia da het han");
        }

        maGiamGia.setTrangThai("DA_SU_DUNG");
        maGiamGia.setNgaySuDung(now);
        return maGiamGiaRepository.save(maGiamGia);
    }

    @Override
    public BigDecimal tinhTienSauMaGiamGia(BigDecimal tongTien, MaGiamGia maGiamGia) {
        if (tongTien == null || tongTien.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        if (maGiamGia == null || maGiamGia.getGiaTriGiam() == null) {
            return tongTien;
        }
        BigDecimal tongSauGiam = tongTien.subtract(maGiamGia.getGiaTriGiam());
        return tongSauGiam.max(BigDecimal.ZERO);
    }

    @Override
    @Transactional
    public void deleteUsedById(int id) {
        MaGiamGia maGiamGia = maGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay ma giam gia co id = " + id));
        if (!"DA_SU_DUNG".equals(maGiamGia.getTrangThai())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chi co the xoa ma giam gia da su dung");
        }
        hoaDonRepository.clearMaGiamGiaReferences(id);
        maGiamGiaRepository.delete(maGiamGia);
    }

    private String taoMaGiamGia() {
        String ma;
        do {
            ma = "MGG-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase(Locale.ROOT);
        } while (maGiamGiaRepository.existsByMa(ma));
        return ma;
    }
}
