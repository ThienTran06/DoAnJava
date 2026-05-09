package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.TacGia;
import com.library.librarymanager.entity.TheLoai;
import com.library.librarymanager.repository.TacGiaRepository;
import com.library.librarymanager.service.TacGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TacGiaServiceImpl implements TacGiaService {
    private final TacGiaRepository tacGiaRepository;
    @Override
    public List<TacGia> getAll() {
        return tacGiaRepository.findAll();
    }

    @Override
    public TacGia getById(int id) {
        return tacGiaRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy tác giả có id = "+id));
    }

    @Override
    public TacGia create(TacGia tacGia) {
        return tacGiaRepository.save(tacGia);
    }

    @Override
    public TacGia updateById(int id, TacGia tacGia) {
        TacGia res = tacGiaRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy tác giả có id = "+id));
        res.setHoTen(tacGia.getHoTen());
        res.setQuocTich(tacGia.getQuocTich());
        tacGiaRepository.save(res);
        return res;
    }

    @Override
    public void deleteById(int id) {
        tacGiaRepository.deleteById(id);
    }
}
