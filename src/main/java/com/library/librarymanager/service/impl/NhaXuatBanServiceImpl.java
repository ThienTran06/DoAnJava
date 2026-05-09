package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.NhaXuatBan;
import com.library.librarymanager.repository.NhaXuatBanRepository;
import com.library.librarymanager.service.NhaXuatBanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NhaXuatBanServiceImpl implements NhaXuatBanService {
    private final NhaXuatBanRepository nhaXuatBanRepository;
    @Override
    public List<NhaXuatBan> getAll() {
        return nhaXuatBanRepository.findAll();
    }

    @Override
    public NhaXuatBan getById(int id) {
        return nhaXuatBanRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy nhà xuất bản có id = "+id));
    }

    @Override
    public NhaXuatBan create(NhaXuatBan nhaXuatBan) {
        return nhaXuatBanRepository.save(nhaXuatBan);
    }

    @Override
    public NhaXuatBan updateById(int id, NhaXuatBan nhaXuatBan) {
        NhaXuatBan res = nhaXuatBanRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy nhà xuất bản có id = "+id));
        res.setDiaChi(nhaXuatBan.getDiaChi());
        res.setTenNXB(nhaXuatBan.getTenNXB());
        nhaXuatBanRepository.save(res);
        return res;
    }

    @Override
    public void deleteById(int id) {
        nhaXuatBanRepository.deleteById(id);
    }
}
