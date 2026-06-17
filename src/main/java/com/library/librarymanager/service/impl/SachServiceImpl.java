package com.library.librarymanager.service.impl;

import com.library.librarymanager.dto.response.SachBanChayResponse;
import com.library.librarymanager.dto.response.SachThongKeResponse;
import com.library.librarymanager.dto.response.SachTonKhoResponse;
import com.library.librarymanager.entity.NhaXuatBan;
import com.library.librarymanager.entity.TacGia;
import com.library.librarymanager.entity.TheLoai;
import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.repository.ChiTietHoaDonRepository;
import com.library.librarymanager.repository.NhaXuatBanRepository;
import com.library.librarymanager.repository.TacGiaRepository;
import com.library.librarymanager.repository.TheLoaiRepository;
import com.library.librarymanager.repository.SachRepository;
import com.library.librarymanager.service.Interface.CloudinaryService;
import com.library.librarymanager.service.Interface.SachService;
import com.library.librarymanager.service.Interface.UuDaiService;
import com.library.librarymanager.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SachServiceImpl implements SachService {
    private final SachRepository sachRepository;
    private final TheLoaiRepository theLoaiRepository;
    private final NhaXuatBanRepository nhaXuatBanRepository;
    private final TacGiaRepository tacGiaRepository;
    private final CloudinaryService cloudinaryService;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final UuDaiService uuDaiService;

    @Override
    public List<Sach> getAll() {
        List<Sach> list = sachRepository.findAll();
        list.forEach(uuDaiService::ganUuDaiHienTai);
        return list;
    }

    @Override
    public Sach getById(int id) {
        Sach sach = sachRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay sach co id = " + id));
        uuDaiService.ganUuDaiHienTai(sach);
        return sach;
    }

    @Override
    @Transactional
    public Sach create(Sach sach) {
        validateSach(sach, null);
        resolveSachReferences(sach);
        return sachRepository.save(sach);
    }

    @Override
    @Transactional
    public Sach create(Sach sach, MultipartFile fileAnh) {
        String imageUrl = uploadImageIfPresent(fileAnh);
        if (imageUrl != null) {
            sach.setHinhAnh(imageUrl);
        }
        validateSach(sach, null);
        resolveSachReferences(sach);
        return sachRepository.save(sach);
    }

    @Override
    @Transactional
    public Sach updateById(int id, Sach sach) {
        Sach res = getById(id);
        validateSach(sach, id);
        resolveSachReferences(sach);
        res.setTenSach(sach.getTenSach());
        if (sach.getHinhAnh() != null) {
            res.setHinhAnh(sach.getHinhAnh());
        }
        res.setGiaBan(sach.getGiaBan());
        res.setNamXuatBan(sach.getNamXuatBan());
        res.setTheLoai(sach.getTheLoai());
        res.setNhaXuatBan(sach.getNhaXuatBan());
        res.setSoLuongTon(sach.getSoLuongTon());
        res.setDanhSachTacGia(sach.getDanhSachTacGia());
        return sachRepository.save(res);
    }

    @Override
    @Transactional
    public Sach updateById(int id, Sach sach, MultipartFile fileAnh) {
        String imageUrl = uploadImageIfPresent(fileAnh);
        if (imageUrl != null) {
            sach.setHinhAnh(imageUrl);
        }
        return updateById(id, sach);
    }

    @Override
    public void deleteById(int id) {
        sachRepository.deleteById(id);
    }

    @Override
    public List<Sach> search(String tenSach, String tenTheLoai, String tenTacGia, Integer namXuatBan) {
        List<Sach> list = sachRepository.search(tenSach, tenTheLoai, tenTacGia, namXuatBan);
        list.forEach(uuDaiService::ganUuDaiHienTai);
        return list;
    }

    @Override
    public List<SachTonKhoResponse> getStockByName(String tenSach) {
        return sachRepository.tonKhoTheoTen(tenSach);
    }

    @Override
    public List<SachTonKhoResponse> getTonKhoNhieuNhat() {
        return sachRepository.tonKhoNhieuNhat();
    }

    @Override
    public List<SachTonKhoResponse> getTonKhoIt() {
        return sachRepository.tonKhoIt();
    }

    @Override
    public Integer getTongSoLuongTon() {
        return sachRepository.getTongSoLuongTon();
    }

    @Override
    public SachThongKeResponse getThongKeSach() {
        List<SachBanChayResponse> sachBanChay = chiTietHoaDonRepository.sachBanChay();
        SachBanChayResponse top = sachBanChay.isEmpty() ? null : sachBanChay.get(0);
        Integer tongSoLuongTon = sachRepository.getTongSoLuongTon();
        BigDecimal giaTriKho = sachRepository.getGiaTriKho();

        return new SachThongKeResponse(
                sachRepository.count(),
                tongSoLuongTon == null ? 0 : tongSoLuongTon,
                sachRepository.countBySoLuongTon(0),
                giaTriKho == null ? BigDecimal.ZERO : giaTriKho,
                top == null ? null : top.getTenSach(),
                top == null ? 0 : top.getSoLuongDaBan()
        );
    }

    @Override
    public Page<Sach> getDanhSachSach(String keyword, int page, int size) {
        return sachRepository.search(keyword, PageRequest.of(page, size))
                .map(sach -> {
                    uuDaiService.ganUuDaiHienTai(sach);
                    return sach;
                });
    }

    @Override
    public Page<Sach> getTatCaSach(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return sachRepository.searchAll(keyword, pageable)
                .map(sach -> {
                    uuDaiService.ganUuDaiHienTai(sach);
                    return sach;
                });
    }

    private void validateSach(Sach sach, Integer currentId) {
        if (sach == null) {
            throw ValidationUtils.badRequest("Du lieu sach khong duoc de trong");
        }
        String tenSach = ValidationUtils.requireText(sach.getTenSach(), "Ten sach");
        boolean duplicated = currentId == null
                ? sachRepository.existsByTenSachIgnoreCase(tenSach)
                : sachRepository.existsByTenSachIgnoreCaseAndIdNot(tenSach, currentId);
        if (duplicated) {
            throw ValidationUtils.badRequest("Sach da ton tai");
        }
        sach.setHinhAnh(normalizeBookImage(sach.getHinhAnh()));
        ValidationUtils.requirePositive(sach.getGiaBan(), "Gia ban");
        ValidationUtils.requirePositiveOrZero(sach.getSoLuongTon(), "So luong ton");
        ValidationUtils.requirePublicationYear(sach.getNamXuatBan());
        if (sach.getTheLoai() == null || sach.getTheLoai().getId() <= 0) {
            throw ValidationUtils.badRequest("Vui long chon the loai");
        }
        if (sach.getNhaXuatBan() == null || sach.getNhaXuatBan().getId() <= 0) {
            throw ValidationUtils.badRequest("Vui long chon nha xuat ban");
        }
        if (sach.getDanhSachTacGia() == null || sach.getDanhSachTacGia().isEmpty()) {
            throw ValidationUtils.badRequest("Vui long chon it nhat mot tac gia");
        }
        boolean invalidAuthor = sach.getDanhSachTacGia().stream()
                .anyMatch(tacGia -> tacGia == null || tacGia.getId() <= 0);
        if (invalidAuthor) {
            throw ValidationUtils.badRequest("Tac gia khong hop le");
        }
        sach.setTenSach(tenSach);
    }

    private void resolveSachReferences(Sach sach) {
        TheLoai theLoai = loadTheLoai(sach.getTheLoai().getId());
        NhaXuatBan nhaXuatBan = loadNhaXuatBan(sach.getNhaXuatBan().getId());
        List<TacGia> danhSachTacGia = resolveTacGiaList(sach.getDanhSachTacGia());

        sach.setTheLoai(theLoai);
        sach.setNhaXuatBan(nhaXuatBan);
        sach.setDanhSachTacGia(danhSachTacGia);
    }

    private TheLoai loadTheLoai(int id) {
        return theLoaiRepository.findById(id)
                .orElseThrow(() -> ValidationUtils.badRequest("Khong tim thay the loai co id = " + id));
    }

    private NhaXuatBan loadNhaXuatBan(int id) {
        return nhaXuatBanRepository.findById(id)
                .orElseThrow(() -> ValidationUtils.badRequest("Khong tim thay nha xuat ban co id = " + id));
    }

    private List<TacGia> resolveTacGiaList(List<TacGia> tacGiaList) {
        if (tacGiaList == null) {
            throw ValidationUtils.badRequest("Vui long chon it nhat mot tac gia");
        }

        Set<Integer> tacGiaIds = new LinkedHashSet<>();
        for (TacGia tacGia : tacGiaList) {
            if (tacGia == null || tacGia.getId() <= 0) {
                throw ValidationUtils.badRequest("Tac gia khong hop le");
            }
            tacGiaIds.add(tacGia.getId());
        }

        if (tacGiaIds.isEmpty()) {
            throw ValidationUtils.badRequest("Vui long chon it nhat mot tac gia");
        }

        List<TacGia> resolved = new ArrayList<>();
        for (Integer tacGiaId : tacGiaIds) {
            resolved.add(tacGiaRepository.findById(tacGiaId)
                    .orElseThrow(() -> ValidationUtils.badRequest("Khong tim thay tac gia co id = " + tacGiaId)));
        }
        return resolved;
    }

    private String normalizeBookImage(String imageUrl) {
        String value = ValidationUtils.trimToNull(imageUrl);
        if (value == null) {
            return null;
        }
        String lower = value.toLowerCase();
        if (lower.contains("zalo") || lower.contains("zaloapp") || lower.contains("zalo.me")) {
            return null;
        }
        return value;
    }

    private String uploadImageIfPresent(MultipartFile fileAnh) {
        if (fileAnh == null || fileAnh.isEmpty()) {
            return null;
        }
        String contentType = fileAnh.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File phai la anh");
        }
        if (fileAnh.getSize() > 5 * 1024 * 1024) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anh vuot qua 5MB");
        }
        return cloudinaryService.uploadFile(fileAnh);
    }
}
