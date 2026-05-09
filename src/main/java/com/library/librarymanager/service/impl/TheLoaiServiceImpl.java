package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.TheLoai;
import com.library.librarymanager.repository.TheLoaiRepository;
import com.library.librarymanager.service.TheLoaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service
@RequiredArgsConstructor
public class TheLoaiServiceImpl implements TheLoaiService {
    private final TheLoaiRepository theLoaiRepository;
    @Override
    public List<TheLoai> getAll() {
        return theLoaiRepository.findAll();
    }

    @Override
    public TheLoai getById(int id) {
        return theLoaiRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy thể loại có id = "+id));
    }

    @Override
    public TheLoai create(TheLoai theLoai) {
        return theLoaiRepository.save(theLoai);
    }

    @Override
    public TheLoai updateById(int id, TheLoai theLoai) {
        TheLoai res = theLoaiRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy thể loại có id = "+id));
        res.setTenTheLoai(theLoai.getTenTheLoai());
        res.setMoTa(theLoai.getMoTa());
        theLoaiRepository.save(res);
        return res;
    }

    @Override
    public void deleteById(int id) {
        theLoaiRepository.deleteById(id);
    }
}
