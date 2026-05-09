package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.ChiTietHoaDon;
import com.library.librarymanager.repository.ChiTietHoaDonRepository;
import com.library.librarymanager.service.ChiTietHoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChiTietHoaDonServiceImpl implements ChiTietHoaDonService {
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    @Override
    public List<ChiTietHoaDon> getAll() {
        return chiTietHoaDonRepository.findAll();
    }

    @Override
    public ChiTietHoaDon getById(int id) {
        return chiTietHoaDonRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy chi tiết phiếu nhập có id = "+id));
    }

    @Override
    public ChiTietHoaDon create(ChiTietHoaDon ChiTietHoaDon) {
        return chiTietHoaDonRepository.save(ChiTietHoaDon);
    }

    @Override
    public ChiTietHoaDon updateById(int id, ChiTietHoaDon ChiTietHoaDon) {
        ChiTietHoaDon res = chiTietHoaDonRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy chi tiết phiếu nhập có id = "+id));
        res.setDonGia(ChiTietHoaDon.getDonGia());
        res.setSoLuong(ChiTietHoaDon.getSoLuong());
        chiTietHoaDonRepository.save(res);
        return res;
    }

    @Override
    public void deleteById(int id) {
        chiTietHoaDonRepository.deleteById(id);
    }
}
