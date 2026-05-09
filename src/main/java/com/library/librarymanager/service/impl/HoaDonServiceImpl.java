package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.HoaDon;
import com.library.librarymanager.repository.HoaDonRepository;
import com.library.librarymanager.service.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor

public class HoaDonServiceImpl implements HoaDonService {
    private final HoaDonRepository hoaDonRepository;
    @Override
    public List<HoaDon> getAll() {
        return hoaDonRepository.findAll();
    }

    @Override
    public HoaDon getById(int id) {
        return hoaDonRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy nhà cung cấp có id = "+id));
    }

    @Override
    public HoaDon create(HoaDon hoaDon) {
        return hoaDonRepository.save(hoaDon);
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
}
