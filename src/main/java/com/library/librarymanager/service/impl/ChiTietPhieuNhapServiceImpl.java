package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.ChiTietPhieuNhap;
import com.library.librarymanager.repository.ChiTietPhieuNhapRepository;
import com.library.librarymanager.service.ChiTietPhieuNhapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChiTietPhieuNhapServiceImpl implements ChiTietPhieuNhapService {
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    @Override
    public List<ChiTietPhieuNhap> getAll() {
        return chiTietPhieuNhapRepository.findAll();
    }

    @Override
    public ChiTietPhieuNhap getById(int id) {
        return chiTietPhieuNhapRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy chi tiết phiếu nhập có id = "+id));
    }

    @Override
    public ChiTietPhieuNhap create(ChiTietPhieuNhap chiTietPhieuNhap) {
        return chiTietPhieuNhapRepository.save(chiTietPhieuNhap);
    }

    @Override
    public ChiTietPhieuNhap updateById(int id, ChiTietPhieuNhap chiTietPhieuNhap) {
        ChiTietPhieuNhap res = chiTietPhieuNhapRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy chi tiết phiếu nhập có id = "+id));
        res.setGiaNhap(chiTietPhieuNhap.getGiaNhap());
        res.setSoLuong(chiTietPhieuNhap.getSoLuong());
        chiTietPhieuNhapRepository.save(res);
        return res;
    }

    @Override
    public void deleteById(int id) {
        chiTietPhieuNhapRepository.deleteById(id);
    }
}
