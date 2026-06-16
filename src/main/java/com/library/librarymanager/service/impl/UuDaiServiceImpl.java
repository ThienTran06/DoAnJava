package com.library.librarymanager.service.impl;

import com.library.librarymanager.dto.request.UuDaiRequest;
import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.entity.UuDai;
import com.library.librarymanager.repository.SachRepository;
import com.library.librarymanager.repository.UuDaiRepository;
import com.library.librarymanager.service.Interface.UuDaiService;
import com.library.librarymanager.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UuDaiServiceImpl implements UuDaiService {
    private final UuDaiRepository uuDaiRepository;
    private final SachRepository sachRepository;

    @Override
    public List<UuDai> getAll() {
        return uuDaiRepository.findAllByOrderByNgayBatDauDescIdDesc();
    }

    @Override
    public UuDai create(UuDaiRequest request) {
        validateRequest(request);
        List<Sach> danhSachSach = sachRepository.findAllById(request.getSachIds());
        if (danhSachSach.size() != new HashSet<>(request.getSachIds()).size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Danh sach sach ap dung uu dai khong hop le");
        }

        UuDai uuDai = new UuDai();
        uuDai.setTenUuDai(ValidationUtils.requireText(request.getTenUuDai(), "Ten uu dai"));
        uuDai.setPhanTramGiam(request.getPhanTramGiam());
        uuDai.setNgayBatDau(request.getNgayBatDau());
        uuDai.setNgayKetThuc(request.getNgayKetThuc());
        uuDai.setTrangThai(request.getTrangThai() == null || request.getTrangThai());
        uuDai.setDanhSachSach(danhSachSach);
        return uuDaiRepository.save(uuDai);
    }

    @Override
    public UuDai getActiveForSach(int sachId) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        List<UuDai> activePromotions = uuDaiRepository.findActiveBySachId(sachId, today);
        return activePromotions.isEmpty() ? null : activePromotions.get(0);
    }

    @Override
    public BigDecimal tinhGiaSauUuDai(Sach sach) {
        if (sach == null || sach.getGiaBan() == null) {
            return BigDecimal.ZERO;
        }
        UuDai uuDai = getActiveForSach(sach.getId());
        if (uuDai == null) {
            return sach.getGiaBan();
        }
        return tinhGiaSauUuDai(sach.getGiaBan(), uuDai.getPhanTramGiam());
    }

    @Override
    public void ganUuDaiHienTai(Sach sach) {
        if (sach == null) {
            return;
        }
        UuDai uuDai = getActiveForSach(sach.getId());
        if (uuDai == null) {
            sach.setGiaSauUuDai(sach.getGiaBan());
            sach.setPhanTramUuDai(BigDecimal.ZERO);
            sach.setTenUuDai(null);
            return;
        }
        sach.setGiaSauUuDai(tinhGiaSauUuDai(sach.getGiaBan(), uuDai.getPhanTramGiam()));
        sach.setPhanTramUuDai(uuDai.getPhanTramGiam());
        sach.setTenUuDai(uuDai.getTenUuDai());
    }

    private void validateRequest(UuDaiRequest request) {
        if (request == null) {
            throw ValidationUtils.badRequest("Du lieu uu dai khong duoc de trong");
        }
        ValidationUtils.requireText(request.getTenUuDai(), "Ten uu dai");
        if (request.getPhanTramGiam() == null
                || request.getPhanTramGiam().compareTo(BigDecimal.ZERO) <= 0
                || request.getPhanTramGiam().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw ValidationUtils.badRequest("Phan tram giam phai nam trong khoang 0 den 100");
        }
        if (request.getNgayBatDau() == null || request.getNgayKetThuc() == null) {
            throw ValidationUtils.badRequest("Ngay ap dung uu dai khong duoc de trong");
        }
        if (request.getNgayBatDau().isAfter(request.getNgayKetThuc())) {
            throw ValidationUtils.badRequest("Ngay bat dau khong duoc lon hon ngay ket thuc");
        }
        if (request.getSachIds() == null || request.getSachIds().isEmpty()) {
            throw ValidationUtils.badRequest("Vui long chon it nhat mot sach ap dung uu dai");
        }
        if (request.getSachIds().stream().anyMatch(id -> id == null || id <= 0)) {
            throw ValidationUtils.badRequest("Danh sach sach ap dung uu dai khong hop le");
        }
    }

    private BigDecimal tinhGiaSauUuDai(BigDecimal giaBan, BigDecimal phanTramGiam) {
        BigDecimal tiLeThanhToan = BigDecimal.valueOf(100).subtract(phanTramGiam)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return giaBan.multiply(tiLeThanhToan).setScale(0, RoundingMode.HALF_UP);
    }
}
