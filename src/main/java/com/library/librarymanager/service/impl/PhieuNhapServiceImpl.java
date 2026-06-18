package com.library.librarymanager.service.impl;

import com.library.librarymanager.dto.request.ChiTietPhieuNhapRequest;
import com.library.librarymanager.dto.request.PhieuNhapRequest;
import com.library.librarymanager.dto.request.UpdatePhieuNhapRequest;
import com.library.librarymanager.dto.response.ThongKePhieuNhapResponse;
import com.library.librarymanager.entity.ChiTietPhieuNhap;
import com.library.librarymanager.entity.NguoiDung;
import com.library.librarymanager.entity.NhaCungCap;
import com.library.librarymanager.entity.PhieuNhap;
import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.event.NotificationEvent;
import com.library.librarymanager.event.StockChangedEvent;
import com.library.librarymanager.repository.ChiTietPhieuNhapRepository;
import com.library.librarymanager.repository.NguoiDungRepository;
import com.library.librarymanager.repository.NhaCungCapRepository;
import com.library.librarymanager.repository.PhieuNhapRepository;
import com.library.librarymanager.repository.SachRepository;
import com.library.librarymanager.service.Interface.PhieuNhapService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PhieuNhapServiceImpl implements PhieuNhapService {
    private final PhieuNhapRepository phieuNhapRepository;
    private final NhaCungCapRepository nhaCungCapRepository;
    private final NguoiDungRepository nhanVienRepository;
    private final SachRepository sachRepository;
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    private final ApplicationEventPublisher publisher;
    @Override
    public List<PhieuNhap> getAll() {
        return phieuNhapRepository.findAll();
    }

    @Override
    public Page<PhieuNhap> getAll(Integer id, LocalDate ngay, int page, int size) {
        LocalDateTime tuNgay = ngay == null ? null : ngay.atStartOfDay();
        LocalDateTime denNgay = ngay == null ? null : ngay.plusDays(1).atStartOfDay();
        return phieuNhapRepository.getAll(id, tuNgay, denNgay, PageRequest.of(page, size));
    }

    @Override
    public PhieuNhap getById(int id) {
        return phieuNhapRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay phieu nhap co id = " + id));
    }

    @Override
    @Transactional
    public PhieuNhap create(PhieuNhapRequest request) {
        validatePhieuNhapRequest(request);
        NhaCungCap nhaCungCap = nhaCungCapRepository.findById(request.getNhaCungCapId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong tim thay nha cung cap co id = " + request.getNhaCungCapId()));
        NguoiDung nhanVien = nhanVienRepository.findById(request.getNhanVienId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong tim thay nhan vien co id = " + request.getNhanVienId()));

        PhieuNhap phieuNhap = new PhieuNhap();
        phieuNhap.setTrangThai("HOAN THANH");
        phieuNhap.setNhaCungCap(nhaCungCap);
        phieuNhap.setNhanVien(nhanVien);
        phieuNhap.setNgayNhap(LocalDateTime.now());
        phieuNhapRepository.save(phieuNhap);

        BigDecimal tongTienNhap = BigDecimal.ZERO;
        List<ChiTietPhieuNhap> list = new ArrayList<>();

        for (ChiTietPhieuNhapRequest detailRequest : request.getList()) {
            Sach sach = sachRepository.findByIdForUpdate(detailRequest.getSachId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong tim thay sach co id = " + detailRequest.getSachId()));

            ChiTietPhieuNhap chiTietPhieuNhap = new ChiTietPhieuNhap();
            chiTietPhieuNhap.setPhieuNhap(phieuNhap);
            chiTietPhieuNhap.setSach(sach);
            chiTietPhieuNhap.setSoLuong(detailRequest.getSoLuongNhap());
            chiTietPhieuNhap.setGiaNhap(detailRequest.getGiaNhap());

            sach.setSoLuongTon(sach.getSoLuongTon() + detailRequest.getSoLuongNhap());
            tongTienNhap = tongTienNhap.add(detailRequest.getGiaNhap().multiply(BigDecimal.valueOf(detailRequest.getSoLuongNhap())));
            list.add(chiTietPhieuNhap);
        }

        chiTietPhieuNhapRepository.saveAll(list);
        phieuNhap.setTongTien(tongTienNhap);
        phieuNhap.setDanhSachChiTiet(list);
        publisher.publishEvent(new StockChangedEvent());
        publisher.publishEvent(new NotificationEvent(
                "import", "Phiếu nhập mới",
                "Đã tạo phiếu nhập #PN" + phieuNhap.getId() + " với " + request.getList().size() + " mặt hàng.",
                "NhapHang.html"));
        return phieuNhapRepository.save(phieuNhap);
    }

    @Override
    public PhieuNhap updateById(int id, UpdatePhieuNhapRequest request) {
        PhieuNhap res = phieuNhapRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay phieu nhap co id = " + id));
        if("DA HUY".equals(res.getTrangThai()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không thể cập nhật phiếu nhập đã bị hủy");
        if (request.getNgayNhap() != null) {
            res.setNgayNhap(request.getNgayNhap());
        }

        if (request.getNhanVienId() != null) {
            NguoiDung nhanVien = nhanVienRepository.findById(request.getNhanVienId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong tim thay nhan vien co id = " + request.getNhanVienId()));
            res.setNhanVien(nhanVien);
        }

        if (request.getNhaCungCapId() != null) {
            NhaCungCap nhaCungCap = nhaCungCapRepository.findById(request.getNhaCungCapId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong tim thay nha cung cap co id = " + request.getNhaCungCapId()));
            res.setNhaCungCap(nhaCungCap);
        }

        return phieuNhapRepository.save(res);
    }

    @Override
    @Transactional
    public PhieuNhap updateChiTiet(int id, PhieuNhapRequest request) {
        validatePhieuNhapRequest(request);
        PhieuNhap phieuNhap = phieuNhapRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay phieu nhap co id = " + id));
        if("DA HUY".equals(phieuNhap.getTrangThai()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Khônng thể cập nhật danh sách chi tiết phiếu nhập đã bị hủy");
        List<ChiTietPhieuNhap> oldDetails = new ArrayList<>(phieuNhap.getDanhSachChiTiet());

        for (ChiTietPhieuNhap oldDetail : oldDetails) {
            Sach sach = sachRepository.findByIdForUpdate(oldDetail.getSach().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong tim thay sach co id = " + oldDetail.getSach().getId()));

            if (sach.getSoLuongTon() < oldDetail.getSoLuong()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong the cap nhat vi ton kho hien tai nho hon so luong nhap cu cua sach id = " + sach.getId());
            }

            sach.setSoLuongTon(sach.getSoLuongTon() - oldDetail.getSoLuong());
        }

        chiTietPhieuNhapRepository.deleteAll(oldDetails);
        phieuNhap.getDanhSachChiTiet().clear();

        BigDecimal tongTienNhap = BigDecimal.ZERO;
        List<ChiTietPhieuNhap> newDetails = new ArrayList<>();

        for (ChiTietPhieuNhapRequest detailRequest : request.getList()) {
            Sach sach = sachRepository.findByIdForUpdate(detailRequest.getSachId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong tim thay sach co id = " + detailRequest.getSachId()));

            ChiTietPhieuNhap chiTietPhieuNhap = new ChiTietPhieuNhap();
            chiTietPhieuNhap.setPhieuNhap(phieuNhap);
            chiTietPhieuNhap.setSach(sach);
            chiTietPhieuNhap.setSoLuong(detailRequest.getSoLuongNhap());
            chiTietPhieuNhap.setGiaNhap(detailRequest.getGiaNhap());

            sach.setSoLuongTon(sach.getSoLuongTon() + detailRequest.getSoLuongNhap());
            tongTienNhap = tongTienNhap.add(detailRequest.getGiaNhap().multiply(BigDecimal.valueOf(detailRequest.getSoLuongNhap())));
            newDetails.add(chiTietPhieuNhap);
        }

        chiTietPhieuNhapRepository.saveAll(newDetails);
        phieuNhap.setDanhSachChiTiet(newDetails);
        phieuNhap.setTongTien(tongTienNhap);

        return phieuNhapRepository.save(phieuNhap);
    }
    @Override
    @Transactional
    public void huyPhieuNhap(int id) {
        PhieuNhap phieuNhap = phieuNhapRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Khong tim thay phieu nhap co id = " + id
                ));

        if ("DA HUY".equals(phieuNhap.getTrangThai())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Phieu nhap da bi huy roi"
            );
        }

        for (ChiTietPhieuNhap chiTiet : phieuNhap.getDanhSachChiTiet()) {
            Sach sach = sachRepository.findByIdForUpdate(chiTiet.getSach().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Khong tim thay sach co id = " + chiTiet.getSach().getId()
                    ));

            if (sach.getSoLuongTon() < chiTiet.getSoLuong()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Khong du so luong ton de huy phieu nhap"
                );
            }

            sach.setSoLuongTon(sach.getSoLuongTon() - chiTiet.getSoLuong());
        }

        phieuNhap.setTrangThai("DA HUY");
        phieuNhapRepository.save(phieuNhap);
    }
    @Override
    @Transactional
    public void deleteById(int id) {
        PhieuNhap phieuNhap = phieuNhapRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Khong tim thay phieu nhap co id = " + id
                ));

        if (!"DA HUY".equals(phieuNhap.getTrangThai())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Hien tai chi co the xoa phieu nhap da bi huy"
            );
        }

        chiTietPhieuNhapRepository.deleteAll(phieuNhap.getDanhSachChiTiet());
        phieuNhapRepository.delete(phieuNhap);
    }
    @Override
    public ThongKePhieuNhapResponse getThongKe() {

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        Object[] rs = (Object[]) phieuNhapRepository.getThongKePhieuNhap(today.atStartOfDay(), today.plusDays(1).atStartOfDay());

        return new ThongKePhieuNhapResponse(
                rs[1] == null ? 0 : ((Number) rs[1]).longValue(),
                rs[0] == null ? 0 : ((Number) rs[0]).longValue(),
                rs[2] == null ? 0 : ((Number) rs[2]).doubleValue(),
                rs[3] == null ? 0 : ((Number) rs[3]).doubleValue()
        );
    }

    private void validatePhieuNhapRequest(PhieuNhapRequest request) {
        if (request == null || request.getList() == null || request.getList().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Danh sach chi tiet phieu nhap khong duoc trong");
        }
        Set<Integer> sachIds = new HashSet<>();
        for (ChiTietPhieuNhapRequest detail : request.getList()) {
            if (detail == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chi tiet phieu nhap khong hop le");
            }
            if (detail.getSoLuongNhap() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "So luong nhap phai la so duong");
            }
            if (detail.getGiaNhap() == null || detail.getGiaNhap().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gia nhap phai la so duong");
            }
            if (!sachIds.add(detail.getSachId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong duoc them trung mot sach trong phieu nhap");
            }
        }
    }
}
