package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.NhaXuatBan;
import com.library.librarymanager.repository.SachRepository;
import com.library.librarymanager.repository.NhaXuatBanRepository;
import com.library.librarymanager.service.Interface.NhaXuatBanService;
import com.library.librarymanager.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NhaXuatBanServiceImpl implements NhaXuatBanService {
    private final NhaXuatBanRepository nhaXuatBanRepository;
    private final SachRepository sachRepository;

    @Override
    public List<NhaXuatBan> getAll() {
        return nhaXuatBanRepository.findAll();
    }

    @Override
    public NhaXuatBan getById(int id) {
        return nhaXuatBanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay nha xuat ban co id = " + id));
    }

    @Override
    public NhaXuatBan create(NhaXuatBan nhaXuatBan) {
        validateNhaXuatBan(nhaXuatBan, null);
        return nhaXuatBanRepository.save(nhaXuatBan);
    }

    @Override
    public NhaXuatBan updateById(int id, NhaXuatBan nhaXuatBan) {
        NhaXuatBan res = getById(id);
        validateNhaXuatBan(nhaXuatBan, id);
        res.setDiaChi(nhaXuatBan.getDiaChi());
        res.setTenNxb(nhaXuatBan.getTenNxb());
        return nhaXuatBanRepository.save(res);
    }

    @Override
    public void deleteById(int id) {
        getById(id);
        if (sachRepository.existsByNhaXuatBanId(id)) {
            throw ValidationUtils.badRequest("Khong the xoa nha xuat ban dang duoc sach su dung");
        }
        try {
            nhaXuatBanRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw ValidationUtils.badRequest("Khong the xoa nha xuat ban dang duoc sach su dung");
        }
    }

    private void validateNhaXuatBan(NhaXuatBan nhaXuatBan, Integer currentId) {
        if (nhaXuatBan == null) {
            throw ValidationUtils.badRequest("Du lieu nha xuat ban khong duoc de trong");
        }
        String tenNxb = ValidationUtils.requireText(nhaXuatBan.getTenNxb(), "Ten nha xuat ban");
        boolean duplicated = currentId == null
                ? nhaXuatBanRepository.existsByTenNxbIgnoreCase(tenNxb)
                : nhaXuatBanRepository.existsByTenNxbIgnoreCaseAndIdNot(tenNxb, currentId);
        if (duplicated) {
            throw ValidationUtils.badRequest("Nha xuat ban da ton tai");
        }
        nhaXuatBan.setTenNxb(tenNxb);
        nhaXuatBan.setDiaChi(ValidationUtils.trimToNull(nhaXuatBan.getDiaChi()));
    }
}
