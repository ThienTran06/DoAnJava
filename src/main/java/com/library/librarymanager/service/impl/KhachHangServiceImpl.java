package com.library.librarymanager.service.impl;

import com.library.librarymanager.Exception.AuthException;
import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.repository.HoaDonRepository;
import com.library.librarymanager.repository.KhachHangRepository;
import com.library.librarymanager.service.Interface.CloudinaryService;
import com.library.librarymanager.service.Interface.KhachHangService;
import com.library.librarymanager.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KhachHangServiceImpl implements KhachHangService {
    private final KhachHangRepository khachHangRepository;
    private final HoaDonRepository hoaDonRepository;
    private final CloudinaryService cloudinaryService;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final BigDecimal MOT_NGAN_DONG = BigDecimal.valueOf(1000);
    private static final BigDecimal VIP_POINT_RATE = BigDecimal.valueOf(1.5);

    @Override
    public List<KhachHang> getAll() {
        List<KhachHang> khachHangs = khachHangRepository.findAll();
        khachHangs.forEach(this::ganTongDonHang);
        return khachHangs;
    }

    @Override
    public KhachHang getById(int id) {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay khach hang co id = " + id));
        ganTongDonHang(khachHang);
        return khachHang;
    }

    @Override
    public KhachHang login(String email, String password) {
        String normalizedEmail = ValidationUtils.trimToNull(email);
        String rawPassword = ValidationUtils.trimToNull(password);

        if (normalizedEmail == null || rawPassword == null) {
            throw new AuthException("Sai email hoặc mật khẩu đăng nhập");
        }

        KhachHang khachHang = khachHangRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new AuthException("Sai email hoặc mật khẩu đăng nhập"));

        if (khachHang.getTrangThai() != null && !khachHang.getTrangThai()) {
            throw new AuthException("Sai email hoặc mật khẩu đăng nhập");
        }

        if (!passwordMatches(rawPassword, khachHang.getPassword())) {
            throw new AuthException("Sai email hoặc mật khẩu đăng nhập");
        }

        return ganTongDonHang(khachHang);
    }

    @Override
    public KhachHang create(KhachHang khachHang) {
        validateKhachHang(khachHang, null);
        apDungHangThanhVienMacDinhNeuCan(khachHang);
        apDungMatKhau(khachHang, khachHang.getPassword(), null);
        return ganTongDonHang(khachHangRepository.save(khachHang));
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
        validateKhachHang(khachHang, res);
        res.setSDT(khachHang.getSDT());
        res.setEmail(khachHang.getEmail());
        res.setHoTen(khachHang.getHoTen());
        if (khachHang.getAvatar() != null) {
            res.setAvatar(khachHang.getAvatar());
        }
        res.setDiemTichLuy(khachHang.getDiemTichLuy());
        res.setHangThanhVien(khachHang.getHangThanhVien());
        res.setVip(khachHang.isVip());
        res.setTrangThai(khachHang.getTrangThai() == null || khachHang.getTrangThai());
        apDungMatKhau(res, khachHang.getPassword(), res.getPassword());
        apDungHangThanhVienMacDinhNeuCan(res);
        return ganTongDonHang(khachHangRepository.save(res));
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

    private void apDungHangThanhVienMacDinhNeuCan(KhachHang khachHang) {
        if (khachHang.getHangThanhVien() == null || khachHang.getHangThanhVien().isBlank()) {
            khachHang.setHangThanhVien(tinhHangThanhVien(khachHang.getDiemTichLuy()));
        }
    }

    private void apDungMatKhau(KhachHang khachHang, String rawPassword, String currentPassword) {
        String normalizedPassword = ValidationUtils.trimToNull(rawPassword);
        if (normalizedPassword == null) {
            khachHang.setPassword(currentPassword);
            return;
        }
        if (normalizedPassword.length() < 4) {
            throw ValidationUtils.badRequest("Mat khau toi thieu 4 ky tu");
        }
        khachHang.setPassword(passwordEncoder.encode(normalizedPassword));
    }

    private boolean passwordMatches(String rawPassword, String storedPassword) {
        String normalizedStoredPassword = ValidationUtils.trimToNull(storedPassword);
        if (normalizedStoredPassword == null) {
            return false;
        }
        if (normalizedStoredPassword.startsWith("$2a$")
                || normalizedStoredPassword.startsWith("$2b$")
                || normalizedStoredPassword.startsWith("$2y$")) {
            try {
                return passwordEncoder.matches(rawPassword, normalizedStoredPassword);
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }
        return normalizedStoredPassword.equals(rawPassword);
    }

    private void validateKhachHang(KhachHang khachHang, KhachHang current) {
        if (khachHang == null) {
            throw ValidationUtils.badRequest("Du lieu khach hang khong duoc de trong");
        }
        String hoTen = ValidationUtils.requireText(khachHang.getHoTen(), "Ho ten khach hang");
        String sdt = ValidationUtils.requireText(khachHang.getSDT(), "So dien thoai");
        ValidationUtils.requirePhone(sdt, "So dien thoai");
        String email = ValidationUtils.trimToNull(khachHang.getEmail());
        ValidationUtils.validateOptionalEmail(email);
        ValidationUtils.requirePositiveOrZero(khachHang.getDiemTichLuy(), "Diem tich luy");

        boolean phoneChanged = current == null || !sdt.equals(current.getSDT());
        boolean duplicatedPhone = phoneChanged && (current == null
                ? khachHangRepository.existsBySdt(sdt)
                : khachHangRepository.existsBySdtAndIdNot(sdt, current.getId()));
        if (duplicatedPhone) {
            throw ValidationUtils.badRequest("So dien thoai khach hang da ton tai");
        }

        if (email != null) {
            String currentEmail = current == null ? null : ValidationUtils.trimToNull(current.getEmail());
            boolean emailChanged = currentEmail == null || !email.equalsIgnoreCase(currentEmail);
            boolean duplicatedEmail = emailChanged && (current == null
                    ? khachHangRepository.existsByEmailIgnoreCase(email)
                    : khachHangRepository.existsByEmailIgnoreCaseAndIdNot(email, current.getId()));
            if (duplicatedEmail) {
                throw ValidationUtils.badRequest("Email khach hang da ton tai");
            }
        }

        khachHang.setHoTen(hoTen);
        khachHang.setSDT(sdt);
        khachHang.setEmail(email);
        khachHang.setHangThanhVien(ValidationUtils.trimToNull(khachHang.getHangThanhVien()));
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
    @Override
    public Page<KhachHang> getDanhSachKhachHang(
            String keyword,
            int page,
            int size
    ) {

        Page<KhachHang> khachHangs = khachHangRepository.search(
                keyword,
                PageRequest.of(page, size)
        );
        khachHangs.forEach(this::ganTongDonHang);
        return khachHangs;
    }

    private KhachHang ganTongDonHang(KhachHang khachHang) {
        if (khachHang != null && khachHang.getId() > 0) {
            khachHang.setTongDonHang(hoaDonRepository.countByKhachHangId(khachHang.getId()));
        }
        return khachHang;
    }
}
