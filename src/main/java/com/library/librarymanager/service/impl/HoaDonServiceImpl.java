package com.library.librarymanager.service.impl;

import com.library.librarymanager.dto.request.ChiTietHoaDonRequest;
import com.library.librarymanager.dto.request.HoaDonRequest;
import com.library.librarymanager.entity.*;
import com.library.librarymanager.repository.*;
import com.library.librarymanager.service.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class HoaDonServiceImpl implements HoaDonService {
    private final HoaDonRepository hoaDonRepository;
    private  final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;
    private final SachRepository sachRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    @Override
    public List<HoaDon> getAll() {
        return hoaDonRepository.findAll();
    }

    @Override
    public HoaDon getById(int id) {
        return hoaDonRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy nhà cung cấp có id = "+id));
    }

    @Override
    public HoaDon create(HoaDonRequest request) {
        NhanVien nhanVien = nhanVienRepository.findById(request.getNhanVienId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy nhân viên có id = "+request.getNhanVienId()));
        KhachHang khachHang = khachHangRepository.findById(request.getKhachHangId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy khách hàng có id = "+request.getKhachHangId()));
        HoaDon newHoaDon = new HoaDon();
        newHoaDon.setNhanVien(nhanVien);
        newHoaDon.setKhachHang(khachHang);
        newHoaDon.setTrangThai("HOAN THANH");
        hoaDonRepository.save(newHoaDon);
        BigDecimal tongTien = BigDecimal.ZERO;
        List<ChiTietHoaDon> list = new ArrayList<>();
        for(ChiTietHoaDonRequest chiTietHoaDonRequest : request.getDanhSachChiTiet()){
            Sach sach= sachRepository.findById(chiTietHoaDonRequest.getSachID()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy sách có id = "+ chiTietHoaDonRequest.getSachID()));
            if(sach.getSoLuongTon()< chiTietHoaDonRequest.getSoLuong())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không đủ sách tồn kho để bán");
            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
            chiTietHoaDon.setSach(sach);
            chiTietHoaDon.setHoaDon(newHoaDon);
            chiTietHoaDon.setSoLuong(chiTietHoaDonRequest.getSoLuong());
            chiTietHoaDon.setDonGia(sach.getGiaBan());
            sach.setSoLuongTon(sach.getSoLuongTon()- chiTietHoaDonRequest.getSoLuong());
            sachRepository.save(sach);
            tongTien=tongTien.add(sach.getGiaBan().multiply(BigDecimal.valueOf(chiTietHoaDonRequest.getSoLuong())));
            list.add(chiTietHoaDon);
        }
        chiTietHoaDonRepository.saveAll(list);
        newHoaDon.setDanhSachChiTiet(list);
        newHoaDon.setNgayBan(LocalDateTime.now());
        newHoaDon.setTongTien(tongTien);
        hoaDonRepository.save(newHoaDon);
        return newHoaDon;
    }


    @Override
    public HoaDon updateById(int id, HoaDon hoaDon) {
        HoaDon res = hoaDonRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy nhà cung cấp có id = "+id));
        res.setMaHoaDon(hoaDon.getMaHoaDon());
        res.setNgayBan(hoaDon.getNgayBan());
        res.setNhanVien(hoaDon.getNhanVien());
        res.setKhachHang(hoaDon.getKhachHang());
        res.setTongTien(hoaDon.getTongTien());
        res.setDanhSachChiTiet(hoaDon.getDanhSachChiTiet());
        res.setTrangThai(hoaDon.getTrangThai());
        hoaDonRepository.save(res);
        return res;
    }

    @Override
    public void deleteById(int id) {
        hoaDonRepository.deleteById(id);
    }

    @Override
    public void huyHoaDon(int id) {
        HoaDon hoaDon =hoaDonRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy hóa đơn có id = "+id));
        if(hoaDon.getTrangThai().equals("DA HUY"))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Đơn đã hủy rồi!");
        for(ChiTietHoaDon chiTietHoaDon : hoaDon.getDanhSachChiTiet()){
            Sach sach = chiTietHoaDon.getSach();
            sach.setSoLuongTon(chiTietHoaDon.getSoLuong()+ sach.getSoLuongTon());
            sachRepository.save(sach);
        }
        hoaDon.setTrangThai("DA HUY");
        hoaDonRepository.save(hoaDon);
    }

}
