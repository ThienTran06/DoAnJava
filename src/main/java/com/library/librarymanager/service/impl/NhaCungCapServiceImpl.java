package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.NhaCungCap;
import com.library.librarymanager.repository.NhaCungCapRepository;
import com.library.librarymanager.service.Interface.NhaCungCapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
@RequiredArgsConstructor
public class NhaCungCapServiceImpl implements NhaCungCapService {
    private final NhaCungCapRepository nhaCungCapRepository;
    @Override
    public List<NhaCungCap> getAll() {
        return nhaCungCapRepository.findAll();
    }

    @Override
    public NhaCungCap getById(int id) {
        return nhaCungCapRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy nhà cung cấp có id = "+id));
    }

    @Override
    public NhaCungCap create(NhaCungCap nhaCungCap) {
        return nhaCungCapRepository.save(nhaCungCap);
    }

    @Override
    public NhaCungCap updateById(int id, NhaCungCap nhaCungCap) {
        NhaCungCap res = nhaCungCapRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy nhà cung cấp có id = "+id));
        res.setDiaChi(nhaCungCap.getDiaChi());
        res.setSDT(nhaCungCap.getSDT());
        res.setTenNCC(nhaCungCap.getTenNCC());
        nhaCungCapRepository.save(res);
        return res;
    }

    @Override
    public void deleteById(int id) {
        nhaCungCapRepository.deleteById(id);
    }
}
