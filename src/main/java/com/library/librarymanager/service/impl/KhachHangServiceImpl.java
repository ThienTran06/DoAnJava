package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.repository.KhachHangRepository;
import com.library.librarymanager.service.Interface.CloudinaryService;
import com.library.librarymanager.service.Interface.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KhachHangServiceImpl implements KhachHangService {
    private final KhachHangRepository khachHangRepository;
    private final CloudinaryService cloudinaryService;

    private static final BigDecimal MOT_NGAN_DONG = BigDecimal.valueOf(1000);
    private static final BigDecimal VIP_POINT_RATE = BigDecimal.valueOf(1.5);

    @Override
    public List<KhachHang> getAll() {
        return khachHangRepository.findAll();
    }

    @Override
    public KhachHang getById(int id) {
        return khachHangRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay khach hang co id = " + id));
    }

    @Override
    public KhachHang create(KhachHang khachHang) {
        capNhatHangThanhVien(khachHang);
        return khachHangRepository.save(khachHang);
    }

    @Override
    public KhachHang create(KhachHang khachHang, MultipartFile avatarFile) {
        String avatarUrl = uploadAvatarIfPresent(avatarFile);
        if (avatarUrl != null) {
            khachHang.setAvatar(avatarUrl);
        }
        return create(khachHang);
    }

    @Override
    public KhachHang updateById(int id, KhachHang khachHang) {
        KhachHang res = khachHangRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay khach hang co id = " + id));
        res.setSDT(khachHang.getSDT());
        res.setEmail(khachHang.getEmail());
        res.setHoTen(khachHang.getHoTen());
        if (khachHang.getAvatar() != null) {
            res.setAvatar(khachHang.getAvatar());
        }
        res.setDiemTichLuy(khachHang.getDiemTichLuy());
        res.setVip(khachHang.isVip());
        capNhatHangThanhVien(res);
        khachHangRepository.save(res);
        return res;
    }

    @Override
    public KhachHang updateById(int id, KhachHang khachHang, MultipartFile avatarFile) {
        String avatarUrl = uploadAvatarIfPresent(avatarFile);
        if (avatarUrl != null) {
            khachHang.setAvatar(avatarUrl);
        }
        return updateById(id, khachHang);
    }

    @Override
    public void deleteById(int id) {
        khachHangRepository.deleteById(id);
    }

    @Override
    public BigDecimal tinhTongTienSauGiamGia(KhachHang khachHang, BigDecimal tongTien) {
        if (tongTien == null || tongTien.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal phanTramGiam = getPhanTramGiamTheoHang(khachHang.getHangThanhVien());
        if (khachHang.isVip()) {
            phanTramGiam = phanTramGiam.add(BigDecimal.valueOf(10));
        }

        BigDecimal heSoThanhToan = BigDecimal.valueOf(100).subtract(phanTramGiam)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return tongTien.multiply(heSoThanhToan).setScale(0, RoundingMode.HALF_UP);
    }

    @Override
    public void congDiemTuHoaDon(KhachHang khachHang, BigDecimal tongTienSauGiamGia) {
        int diemCong = tongTienSauGiamGia.divide(MOT_NGAN_DONG, 0, RoundingMode.DOWN).intValue();
        if (khachHang.isVip()) {
            diemCong = BigDecimal.valueOf(diemCong)
                    .multiply(VIP_POINT_RATE)
                    .setScale(0, RoundingMode.DOWN)
                    .intValue();
        }
        khachHang.setDiemTichLuy(khachHang.getDiemTichLuy() + diemCong);
        capNhatHangThanhVien(khachHang);
        khachHangRepository.save(khachHang);
    }

    @Override
    public void capNhatHangThanhVien(KhachHang khachHang) {
        khachHang.setHangThanhVien(tinhHangThanhVien(khachHang.getDiemTichLuy()));
    }

    @Override
    public String uploadAvatar(MultipartFile avatarFile) {
        String avatarUrl = uploadAvatarIfPresent(avatarFile);
        if (avatarUrl == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Avatar khong duoc trong");
        }
        return avatarUrl;
    }

    private String tinhHangThanhVien(int diemTichLuy) {
        if (diemTichLuy >= 50000) return "Kim Cương";
        if (diemTichLuy >= 20000) return "Bạch Kim";
        if (diemTichLuy >= 5000) return "Vàng";
        if (diemTichLuy >= 1000) return "Bạc";
        return "Đồng";
    }

    private BigDecimal getPhanTramGiamTheoHang(String hangThanhVien) {
        if ("Kim Cương".equalsIgnoreCase(hangThanhVien)) return BigDecimal.valueOf(25);
        if ("Bạch Kim".equalsIgnoreCase(hangThanhVien)) return BigDecimal.valueOf(20);
        if ("Vàng".equalsIgnoreCase(hangThanhVien)) return BigDecimal.valueOf(15);
        if ("Bạc".equalsIgnoreCase(hangThanhVien)) return BigDecimal.valueOf(10);
        return BigDecimal.valueOf(5);
    }

    private String uploadAvatarIfPresent(MultipartFile avatarFile) {
        if (avatarFile == null || avatarFile.isEmpty()) {
            return null;
        }
        String contentType = avatarFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File phai la anh");
        }
        if (avatarFile.getSize() > 5 * 1024 * 1024) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anh vuot qua 5MB");
        }
        return cloudinaryService.uploadFile(avatarFile);
    }
}
