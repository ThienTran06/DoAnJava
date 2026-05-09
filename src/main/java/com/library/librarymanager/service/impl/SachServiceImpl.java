package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.entity.TacGia;
import com.library.librarymanager.repository.SachRepository;
import com.library.librarymanager.repository.TacGiaRepository;
import com.library.librarymanager.service.SachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SachServiceImpl implements SachService
{
    private final SachRepository sachRepository;
    @Override
    public List<Sach> getAll() {
        return sachRepository.findAll();
    }

    @Override
    public Sach getById(int id) {
        return sachRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy sách có id = "+id));
    }

    @Override
    public Sach create(Sach sach) {
        return sachRepository.save(sach);
    }

    @Override
    public Sach updateById(int id, Sach sach) {
        Sach res = sachRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy sách có id = "+id));
        res.setTenSach(sach.getTenSach());
        res.setHinhAnh(sach.getHinhAnh());
        res.setGiaBan(sach.getGiaBan());
        res.setNamXuatBan(sach.getNamXuatBan());
        res.setNhaXuatBan(sach.getNhaXuatBan());
        res.setSoLuongTon(sach.getSoLuongTon());
        res.setDanhSachTacGia(sach.getDanhSachTacGia());
        sachRepository.save(res);
        return res;
    }

    @Override
    public void deleteById(int id) {
        sachRepository.deleteById(id);
    }
}
