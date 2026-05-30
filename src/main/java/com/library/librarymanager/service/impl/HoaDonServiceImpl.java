package com.library.librarymanager.service.impl;

import com.library.librarymanager.dto.request.ChiTietHoaDonRequest;
import com.library.librarymanager.dto.request.HoaDonRequest;
import com.library.librarymanager.dto.request.UpdateHoaDonRequest;
import com.library.librarymanager.dto.response.DoanhThuNamResponse;
import com.library.librarymanager.dto.response.DoanhThuNgayResponse;
import com.library.librarymanager.dto.response.DoanhThuThangResponse;
import com.library.librarymanager.dto.response.DoanhThuTheoTheLoaiResponse;
import com.library.librarymanager.entity.*;
import com.library.librarymanager.repository.*;
import com.library.librarymanager.service.Interface.HoaDonService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class HoaDonServiceImpl implements HoaDonService {
    private final HoaDonRepository hoaDonRepository;
    private  final NguoiDungRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;
    private final SachRepository sachRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    @Override
    public List<HoaDon> getAll() {
        return hoaDonRepository.findAll();
    }

    @Override
    public HoaDon getById(int id) {
        return hoaDonRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy hóa đơn có id = "+id));
    }

    @Override
    @Transactional
    public HoaDon create(HoaDonRequest request) {
        NguoiDung nhanVien = nhanVienRepository.findById(request.getNhanVienId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy nhân viên có id = "+request.getNhanVienId()));
        KhachHang khachHang = khachHangRepository.findById(request.getKhachHangId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy khách hàng có id = "+request.getKhachHangId()));
        HoaDon newHoaDon = new HoaDon();
        newHoaDon.setNhanVien(nhanVien);
        newHoaDon.setKhachHang(khachHang);
        newHoaDon.setTrangThai("HOAN THANH");
        hoaDonRepository.save(newHoaDon);
        BigDecimal tongTien = BigDecimal.ZERO;
        List<ChiTietHoaDon> list = new ArrayList<>();
        for(ChiTietHoaDonRequest chiTietHoaDonRequest : request.getDanhSachChiTiet()){
            Sach sach= sachRepository.findByIdForUpdate(chiTietHoaDonRequest.getSachID()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy sách có id = "+ chiTietHoaDonRequest.getSachID()));
            if(sach.getSoLuongTon()< chiTietHoaDonRequest.getSoLuong())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không đủ sách tồn kho để bán");
            if(chiTietHoaDonRequest.getSoLuong()<=0)throw new IllegalArgumentException("Số lượng không được âm");
            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
            chiTietHoaDon.setSach(sach);
            chiTietHoaDon.setHoaDon(newHoaDon);
            chiTietHoaDon.setSoLuong(chiTietHoaDonRequest.getSoLuong());
            chiTietHoaDon.setDonGia(sach.getGiaBan());
            chiTietHoaDon.setTenSach(sach.getTenSach());
            chiTietHoaDon.setHinhAnh(sach.getHinhAnh());
            chiTietHoaDon.setTenSach(sach.getTenSach());
            chiTietHoaDon.setThanhTien(
                    sach.getGiaBan().multiply(BigDecimal.valueOf(chiTietHoaDonRequest.getSoLuong())));


            sach.setSoLuongTon(sach.getSoLuongTon()- chiTietHoaDonRequest.getSoLuong());

            tongTien=tongTien.add(chiTietHoaDon.getThanhTien());
            list.add(chiTietHoaDon);
        }
        chiTietHoaDonRepository.saveAll(list);
        newHoaDon.setDanhSachChiTiet(list);
        newHoaDon.setNgayBan(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        newHoaDon.setTongTien(tongTien);
        hoaDonRepository.save(newHoaDon);
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
        if("DA HUY".equals(hoaDon.getTrangThai()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Đơn đã hủy rồi!");
        for(ChiTietHoaDon chiTietHoaDon : hoaDon.getDanhSachChiTiet()){
            Sach sach =  sachRepository.findByIdForUpdate(chiTietHoaDon.getSach().getId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy sách có id = "+id));
            sach.setSoLuongTon(chiTietHoaDon.getSoLuong()+ sach.getSoLuongTon());
            sachRepository.save(sach);
        }
        hoaDon.setTrangThai("DA HUY");
        hoaDonRepository.save(hoaDon);
    }
    @Override
    @Transactional
    public HoaDon updateChiTiet(int id, HoaDonRequest request) {
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

            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
            chiTietHoaDon.setHoaDon(hoaDon);
            chiTietHoaDon.setSach(sach);
            chiTietHoaDon.setSoLuong(detailRequest.getSoLuong());
            chiTietHoaDon.setDonGia(sach.getGiaBan());
            chiTietHoaDon.setTenSach(sach.getTenSach());
            chiTietHoaDon.setHinhAnh(sach.getHinhAnh());
            chiTietHoaDon.setThanhTien(sach.getGiaBan().multiply(BigDecimal.valueOf(detailRequest.getSoLuong())));

            sach.setSoLuongTon(sach.getSoLuongTon() - detailRequest.getSoLuong());
            tongTien = tongTien.add(chiTietHoaDon.getThanhTien());
            newDetails.add(chiTietHoaDon);
        }

        chiTietHoaDonRepository.saveAll(newDetails);
        hoaDon.setDanhSachChiTiet(newDetails);
        hoaDon.setTongTien(tongTien);

        return hoaDonRepository.save(hoaDon);
    }

    @Override
    public List<DoanhThuNgayResponse>getDoanhThuTheoNgay(int nam, int thang){
        return hoaDonRepository.doanhThuTheoNgay(nam,thang);
    }
    @Override
    public List<DoanhThuThangResponse>getDoanhThuTheoThang(int nam){
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
        LocalDateTime start = tuNgay.atStartOfDay();
        LocalDateTime end = denNgay.plusDays(1).atStartOfDay();
        return hoaDonRepository.getDoanhThuKhoangNgay(start,end);
    }
    @Override
    public BigDecimal getTongDoanhThu(){
        return hoaDonRepository.getTongDoanhThu();
    }






}
