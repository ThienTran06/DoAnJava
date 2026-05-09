package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.NhanVien;
import com.library.librarymanager.repository.NhanVienRepository;
import com.library.librarymanager.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NhanVienServiceImpl implements NhanVienService {
    private final NhanVienRepository nhanVienRepository;
    @Override
    public List<NhanVien> getAll() {
        return nhanVienRepository.findAll();
    }

    @Override
    public NhanVien getById(int id) {
        return nhanVienRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy nhà xuất bản có id = "+id));
    }

    @Override
    public NhanVien create(NhanVien nhanVien) {
        return nhanVienRepository.save(nhanVien);
    }

    @Override
    public NhanVien updateById(int id, NhanVien nhanVien) {
        NhanVien res = nhanVienRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy nhà xuất bản có id = "+id));
        res.setSDT(nhanVien.getSDT());
        res.setUsername(nhanVien.getUsername());
        res.setPassword(nhanVien.getPassword());
        res.setTrangThai(nhanVien.isTrangThai());
        res.setVaiTro(nhanVien.getVaiTro());
        res.setHoTen(nhanVien.getHoTen());
        nhanVienRepository.save(res);
        return res;
    }

    @Override
    public void deleteById(int id) {
        nhanVienRepository.deleteById(id);
    }
}
