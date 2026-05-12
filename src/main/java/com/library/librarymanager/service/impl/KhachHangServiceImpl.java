package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.repository.KhachHangRepository;
import com.library.librarymanager.service.Interface.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KhachHangServiceImpl implements KhachHangService {
    private final KhachHangRepository khachHangRepository;
    @Override
    public List<KhachHang> getAll() {
        return khachHangRepository.findAll();
    }

    @Override
    public KhachHang getById(int id) {
        return khachHangRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy khách hàng có id = "+id));
    }

    @Override
    public KhachHang create(KhachHang khachHang) {
        return khachHangRepository.save(khachHang);
    }

    @Override
    public KhachHang updateById(int id, KhachHang khachHang) {
        KhachHang res = khachHangRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy khách hàng có id = "+id));
        res.setSDT(khachHang.getSDT());
        res.setEmail(khachHang.getEmail());
        res.setHoTen(khachHang.getHoTen());
        khachHangRepository.save(res);
        return res;
    }

    @Override
    public void deleteById(int id) {
        khachHangRepository.deleteById(id);
    }
}
