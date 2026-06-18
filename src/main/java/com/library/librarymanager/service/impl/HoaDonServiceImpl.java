package com.library.librarymanager.service.impl;

import com.library.librarymanager.dto.request.ChiTietHoaDonRequest;
import com.library.librarymanager.dto.request.HoaDonRequest;
import com.library.librarymanager.dto.request.UpdateHoaDonRequest;
import com.library.librarymanager.dto.response.*;
import com.library.librarymanager.entity.*;
import com.library.librarymanager.event.NotificationEvent;
import com.library.librarymanager.event.StockChangedEvent;
import com.library.librarymanager.repository.*;
import com.library.librarymanager.service.Interface.*;
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

public class HoaDonServiceImpl implements HoaDonService {
    private final HoaDonRepository hoaDonRepository;
    private  final NguoiDungRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;
    private final SachRepository sachRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final KhachHangService khachHangService;
    private final MaGiamGiaService maGiamGiaService;
    private final UuDaiService uuDaiService;
    private final ApplicationEventPublisher publisher;

    @Override
    public List<HoaDon> getAll() {
        return hoaDonRepository.findAll();
    }

    @Override
    public Page<HoaDon> getAll(
            Integer id,
            LocalDate tuNgay,
            LocalDate denNgay,
            String trangThai,
            int page,
            int size
    ) {
        LocalDateTime tu = tuNgay == null ? null : tuNgay.atStartOfDay();
        LocalDateTime den = denNgay == null ? null : denNgay.plusDays(1).atStartOfDay();

        return hoaDonRepository.getAll(
                id,
                tu,
                den,
                trangThai,
                PageRequest.of(page, size)
        );
    }

    @Override
    public HoaDon getById(int id) {
        return hoaDonRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy hóa đơn có id = "+id));
    }

    @Override
    @Transactional
    public HoaDon create(HoaDonRequest request) {
        validateHoaDonRequest(request);
        NguoiDung nhanVien = nhanVienRepository.findById(request.getNhanVienId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy nhân viên có id = "+request.getNhanVienId()));
        KhachHang khachHang = khachHangRepository.findById(request.getKhachHangId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy khách hàng có id = "+request.getKhachHangId()));
        HoaDon newHoaDon = new HoaDon();
        newHoaDon.setNhanVien(nhanVien);
        newHoaDon.setKhachHang(khachHang);
        newHoaDon.setTrangThai("PROCESSING");
        hoaDonRepository.save(newHoaDon);
        BigDecimal tongTien = BigDecimal.ZERO;
        List<ChiTietHoaDon> list = new ArrayList<>();
        for(ChiTietHoaDonRequest chiTietHoaDonRequest : request.getDanhSachChiTiet()){
            Sach sach= sachRepository.findByIdForUpdate(chiTietHoaDonRequest.getSachID()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy sách có id = "+ chiTietHoaDonRequest.getSachID()));
            if(sach.getSoLuongTon()< chiTietHoaDonRequest.getSoLuong())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không đủ sách tồn kho để bán");
            if(chiTietHoaDonRequest.getSoLuong()<=0)throw new IllegalArgumentException("Số lượng không được âm");
            uuDaiService.ganUuDaiHienTai(sach);
            BigDecimal donGiaSauUuDai = sach.getGiaSauUuDai() == null ? sach.getGiaBan() : sach.getGiaSauUuDai();
            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
            chiTietHoaDon.setSach(sach);
            chiTietHoaDon.setHoaDon(newHoaDon);
            chiTietHoaDon.setSoLuong(chiTietHoaDonRequest.getSoLuong());
            chiTietHoaDon.setGiaGoc(sach.getGiaBan());
            chiTietHoaDon.setDonGia(donGiaSauUuDai);
            chiTietHoaDon.setPhanTramUuDai(sach.getPhanTramUuDai());
            chiTietHoaDon.setTenUuDai(sach.getTenUuDai());
            chiTietHoaDon.setTenSach(sach.getTenSach());
            chiTietHoaDon.setHinhAnh(sach.getHinhAnh());
            chiTietHoaDon.setTenSach(sach.getTenSach());
            chiTietHoaDon.setThanhTien(
                    donGiaSauUuDai.multiply(BigDecimal.valueOf(chiTietHoaDonRequest.getSoLuong())));


            sach.setSoLuongTon(sach.getSoLuongTon()- chiTietHoaDonRequest.getSoLuong());

            tongTien=tongTien.add(chiTietHoaDon.getThanhTien());
            list.add(chiTietHoaDon);
        }
        chiTietHoaDonRepository.saveAll(list);
        newHoaDon.setDanhSachChiTiet(list);
        newHoaDon.setNgayBan(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        khachHangService.capNhatHangThanhVien(khachHang);
        BigDecimal tongTienSauGiamGia = khachHangService.tinhTongTienSauGiamGia(khachHang, tongTien);
        MaGiamGia maGiamGia = maGiamGiaService.suDungMa(khachHang, request.getMaGiamGia());
        BigDecimal tongTienCuoi = maGiamGiaService.tinhTienSauMaGiamGia(tongTienSauGiamGia, maGiamGia);
        newHoaDon.setMaGiamGia(maGiamGia);
        newHoaDon.setTienGiamGia(tongTienSauGiamGia.subtract(tongTienCuoi));
        newHoaDon.setTongTien(tongTienCuoi);
        hoaDonRepository.save(newHoaDon);
        khachHangService.congDiemTuHoaDon(khachHang, tongTienCuoi);
        publisher.publishEvent(new StockChangedEvent());
        publisher.publishEvent(new NotificationEvent(
                "invoice", "Hóa đơn mới",
                "Đã tạo hóa đơn #HD" + newHoaDon.getId() + " - " + newHoaDon.getTongTien().toPlainString() + " đ.",
                "HoaDon.html"));
        return newHoaDon;
    }


    @Override
    public HoaDon updateById(int id, UpdateHoaDonRequest request) {
        HoaDon res = hoaDonRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Khong tim thay hoa don co id = "+id));

        if ("DA HUY".equals(res.getTrangThai())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong the cap nhat hoa don da huy");
        }

        if (request.getMaHoaDon() != null) {
            res.setMaHoaDon(request.getMaHoaDon());
        }

        if (request.getNgayBan() != null) {
            res.setNgayBan(request.getNgayBan());
        }

        if (request.getNhanVienId() != null) {
            NguoiDung nhanVien = nhanVienRepository.findById(request.getNhanVienId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong tim thay nhan vien co id = " + request.getNhanVienId()));
            res.setNhanVien(nhanVien);
        }

        if (request.getKhachHangId() != null) {
            KhachHang khachHang = khachHangRepository.findById(request.getKhachHangId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong tim thay khach hang co id = " + request.getKhachHangId()));
            res.setKhachHang(khachHang);
        }

        hoaDonRepository.save(res);
        return res;
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Khong tim thay hoaDon co id = " + id
                ));

        if (!"DA HUY".equals(hoaDon.getTrangThai())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Hien tai chi co the xoa hoaDon da bi huy"
            );
        }

        chiTietHoaDonRepository.deleteAll(hoaDon.getDanhSachChiTiet());
        hoaDonRepository.delete(hoaDon);

    }
    @Transactional
    @Override
    public void huyHoaDon(int id) {
        HoaDon hoaDon =hoaDonRepository.findByIdForUpdate(id).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy hóa đơn có id = "+id));
        if("DA HUY".equals(hoaDon.getTrangThai()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Đơn đã huỷ rồi!");
        for(ChiTietHoaDon chiTietHoaDon : hoaDon.getDanhSachChiTiet()){
            Sach sach =  sachRepository.findByIdForUpdate(chiTietHoaDon.getSach().getId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy sách có id = "+id));
            sach.setSoLuongTon(chiTietHoaDon.getSoLuong()+ sach.getSoLuongTon());
            sachRepository.save(sach);
        }
        hoaDon.setTrangThai("DA HUY");
        hoaDonRepository.save(hoaDon);
        publisher.publishEvent(new StockChangedEvent());
        publisher.publishEvent(new NotificationEvent(
                "invoice", "Hủy hóa đơn",
                "Hóa đơn #HD" + id + " đã bị hủy.",
                "HoaDon.html"));
    }
    @Override
    @Transactional
    public HoaDon updateChiTiet(int id, HoaDonRequest request) {
        validateHoaDonRequest(request);
        HoaDon hoaDon = hoaDonRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay hoa don co id = " + id));

        if ("DA HUY".equals(hoaDon.getTrangThai())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong the cap nhat chi tiet hoa don da huy");
        }

        List<ChiTietHoaDon> oldDetails = new ArrayList<>(hoaDon.getDanhSachChiTiet());

        for (ChiTietHoaDon oldDetail : oldDetails) {
            Sach sach = sachRepository.findByIdForUpdate(oldDetail.getSach().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong tim thay sach co id = " + oldDetail.getSach().getId()));
            sach.setSoLuongTon(sach.getSoLuongTon() + oldDetail.getSoLuong());
        }

        chiTietHoaDonRepository.deleteAll(oldDetails);
        hoaDon.getDanhSachChiTiet().clear();

        BigDecimal tongTien = BigDecimal.ZERO;
        List<ChiTietHoaDon> newDetails = new ArrayList<>();

        for (ChiTietHoaDonRequest detailRequest : request.getDanhSachChiTiet()) {
            Sach sach = sachRepository.findByIdForUpdate(detailRequest.getSachID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong tim thay sach co id = " + detailRequest.getSachID()));

            if (sach.getSoLuongTon() < detailRequest.getSoLuong()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong du sach ton kho de ban");
            }

            uuDaiService.ganUuDaiHienTai(sach);
            BigDecimal donGiaSauUuDai = sach.getGiaSauUuDai() == null ? sach.getGiaBan() : sach.getGiaSauUuDai();
            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
            chiTietHoaDon.setHoaDon(hoaDon);
            chiTietHoaDon.setSach(sach);
            chiTietHoaDon.setSoLuong(detailRequest.getSoLuong());
            chiTietHoaDon.setGiaGoc(sach.getGiaBan());
            chiTietHoaDon.setDonGia(donGiaSauUuDai);
            chiTietHoaDon.setPhanTramUuDai(sach.getPhanTramUuDai());
            chiTietHoaDon.setTenUuDai(sach.getTenUuDai());
            chiTietHoaDon.setTenSach(sach.getTenSach());
            chiTietHoaDon.setHinhAnh(sach.getHinhAnh());
            chiTietHoaDon.setThanhTien(donGiaSauUuDai.multiply(BigDecimal.valueOf(detailRequest.getSoLuong())));

            sach.setSoLuongTon(sach.getSoLuongTon() - detailRequest.getSoLuong());
            tongTien = tongTien.add(chiTietHoaDon.getThanhTien());
            newDetails.add(chiTietHoaDon);
        }

        chiTietHoaDonRepository.saveAll(newDetails);
        hoaDon.setDanhSachChiTiet(newDetails);
        khachHangService.capNhatHangThanhVien(hoaDon.getKhachHang());
        BigDecimal tongTienSauGiamGia = khachHangService.tinhTongTienSauGiamGia(hoaDon.getKhachHang(), tongTien);
        BigDecimal tongTienCuoi = maGiamGiaService.tinhTienSauMaGiamGia(tongTienSauGiamGia, hoaDon.getMaGiamGia());
        hoaDon.setTienGiamGia(tongTienSauGiamGia.subtract(tongTienCuoi));
        hoaDon.setTongTien(tongTienCuoi);
        publisher.publishEvent(new StockChangedEvent());
        return hoaDonRepository.save(hoaDon);
    }

    @Override
    public List<DoanhThuNgayResponse>getDoanhThuTheoNgay(int nam, int thang){
        validateYearMonth(nam, thang);
        return hoaDonRepository.doanhThuTheoNgay(nam,thang);
    }
    @Override
    public List<DoanhThuThangResponse>getDoanhThuTheoThang(int nam){
        validateYear(nam);
        return hoaDonRepository.doanhThuTheoThang(nam);
    }
    @Override
    public List<DoanhThuNamResponse> getDoanhThuTheoNam(){
        return hoaDonRepository.doanhThuTheoNam();
    }
    @Override
    public BigDecimal getDoanhThuHomNay(){
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime startOfNextDay = today.plusDays(1).atStartOfDay();
        BigDecimal doanhThu = hoaDonRepository.doanhThuHomNay(startOfDay, startOfNextDay);
        return doanhThu == null ? BigDecimal.ZERO : doanhThu;
    }
    @Override
    public List<DoanhThuNgayResponse> getDoanhThuBayNgayTruoc(LocalDateTime local){
        return hoaDonRepository.doanhThu7Ngay(local);
    }
    @Override
    public List<DoanhThuNgayResponse> getDoanhThuBaMuoiNgayTruoc(LocalDateTime local){
        return hoaDonRepository.doanhThuBaMuoiNgayTruoc(local);
    }
    @Override
    public List<DoanhThuNgayResponse> getDoanhThuTheoKhoangNgay(LocalDate tuNgay,LocalDate denNgay){
        if (tuNgay == null || denNgay == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngay bao cao khong duoc de trong");
        }
        if (tuNgay.isAfter(denNgay)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tu ngay khong duoc lon hon den ngay");
        }
        LocalDateTime start = tuNgay.atStartOfDay();
        LocalDateTime end = denNgay.plusDays(1).atStartOfDay();
        return hoaDonRepository.getDoanhThuKhoangNgay(start,end);
    }
    @Override
    public BigDecimal getTongDoanhThu(){
        return hoaDonRepository.getTongDoanhThu();
    }
    @Transactional
    @Override
    public ThongKeHoaDonResponse getThongKe() {

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        Object result = hoaDonRepository.getThongKeHoaDon(today.atStartOfDay(), today.plusDays(1).atStartOfDay());
        Object[] row = (Object[]) result;

        long tongHoaDon = row[0] == null ? 0 : ((Number) row[0]).longValue();
        long hoaDonTrongNgay = row[1] == null ? 0 : ((Number) row[1]).longValue();
        BigDecimal doanhThuTrongNgay = (BigDecimal) row[2];
        BigDecimal tongDoanhThu = (BigDecimal) row[3];

        if (doanhThuTrongNgay == null) doanhThuTrongNgay = BigDecimal.ZERO;
        if (tongDoanhThu == null) tongDoanhThu = BigDecimal.ZERO;

        return new ThongKeHoaDonResponse(
                tongHoaDon,
                hoaDonTrongNgay,
                doanhThuTrongNgay,
                tongDoanhThu
        );
    }

    private void validateHoaDonRequest(HoaDonRequest request) {
        if (request == null || request.getDanhSachChiTiet() == null || request.getDanhSachChiTiet().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Danh sach chi tiet hoa don khong duoc trong");
        }
        Set<Integer> sachIds = new HashSet<>();
        for (ChiTietHoaDonRequest detail : request.getDanhSachChiTiet()) {
            if (detail == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chi tiet hoa don khong hop le");
            }
            if (!sachIds.add(detail.getSachID())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong duoc them trung mot sach trong hoa don");
            }
        }
    }

    private void validateYearMonth(int nam, int thang) {
        validateYear(nam);
        if (thang < 1 || thang > 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thang phai nam trong khoang 1 den 12");
        }
    }

    private void validateYear(int nam) {
        int currentYear = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).getYear();
        if (nam < 1000 || nam > currentYear) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nam phai nam trong khoang 1000 den " + currentYear);
        }
    }
    @Override
    public void thanhToanTienMat(int hoaDonId) {
        xacNhanThanhToan(hoaDonId);
    }

    @Override
    public void xacNhanThanhToan(int hoaDonId) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay hoa don"));

        if ("DA HUY".equals(hoaDon.getTrangThai()) || "CANCELLED".equals(hoaDon.getTrangThai())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khong the thanh toan hoa don da huy");
        }

        hoaDon.setTrangThai("PAID");
        hoaDonRepository.save(hoaDon);
    }
    @Override
    public void setPendingStatus(int hoaDonId) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        hoaDon.setTrangThai("PENDING");

        hoaDonRepository.save(hoaDon);
    }
}
