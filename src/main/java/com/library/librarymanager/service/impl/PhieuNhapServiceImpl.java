package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.PhieuNhap;
import com.library.librarymanager.repository.PhieuNhapRepository;;
import com.library.librarymanager.service.PhieuNhapService;
import com.library.librarymanager.service.PhieuNhapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PhieuNhapServiceImpl implements PhieuNhapService {
    private final PhieuNhapRepository phieuNhapRepository;
    @Override
    public List<PhieuNhap> getAll() {
        return phieuNhapRepository.findAll();
    }

    @Override
    public PhieuNhap getById(int id) {
        return phieuNhapRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy phiếu nhập có id = "+id));
    }

    @Override
    public PhieuNhap create(PhieuNhap phieuNhap) {
        return phieuNhapRepository.save(phieuNhap);
    }

    @Override
    public PhieuNhap updateById(int id, PhieuNhap phieuNhap) {
        PhieuNhap res = phieuNhapRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy phiếu nhập có id = "+id));
        res.setMaPhieuNhap(phieuNhap.getMaPhieuNhap());
        res.setNgayNhap(phieuNhap.getNgayNhap());
        res.setTongTien(phieuNhap.getTongTien());
        res.setNhanVien(phieuNhap.getNhanVien());
        res.setNhaCungCap(phieuNhap.getNhaCungCap());
        phieuNhapRepository.save(res);
        return res;
    }

    @Override
    public void deleteById(int id) {
        phieuNhapRepository.deleteById(id);
    }
}
