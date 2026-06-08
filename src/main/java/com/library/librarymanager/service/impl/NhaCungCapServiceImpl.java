package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.NhaCungCap;
import com.library.librarymanager.repository.NhaCungCapRepository;
import com.library.librarymanager.service.Interface.NhaCungCapService;
import com.library.librarymanager.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        return nhaCungCapRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay nha cung cap co id = " + id));
    }

    @Override
    public NhaCungCap create(NhaCungCap nhaCungCap) {
        validateNhaCungCap(nhaCungCap, null);
        return nhaCungCapRepository.save(nhaCungCap);
    }

    @Override
    public NhaCungCap updateById(int id, NhaCungCap nhaCungCap) {
        NhaCungCap res = getById(id);
        validateNhaCungCap(nhaCungCap, id);
        res.setDiaChi(nhaCungCap.getDiaChi());
        res.setSDT(nhaCungCap.getSDT());
        res.setTenNCC(nhaCungCap.getTenNCC());
        return nhaCungCapRepository.save(res);
    }

    @Override
    public void deleteById(int id) {
        nhaCungCapRepository.deleteById(id);
    }

    @Override
    public Page<NhaCungCap> getDanhSachNhaCungCap(String keyword, int page, int size) {
        return nhaCungCapRepository.getDanhSachNhaCungCap(keyword, PageRequest.of(page, size));
    }

    private void validateNhaCungCap(NhaCungCap nhaCungCap, Integer currentId) {
        if (nhaCungCap == null) {
            throw ValidationUtils.badRequest("Du lieu nha cung cap khong duoc de trong");
        }
        String tenNCC = ValidationUtils.requireText(nhaCungCap.getTenNCC(), "Ten nha cung cap");
        String sdt = ValidationUtils.requireText(nhaCungCap.getSDT(), "So dien thoai");
        ValidationUtils.requirePhone(sdt, "So dien thoai");

        boolean duplicatedName = currentId == null
                ? nhaCungCapRepository.existsByTenNCCIgnoreCase(tenNCC)
                : nhaCungCapRepository.existsByTenNCCIgnoreCaseAndIdNot(tenNCC, currentId);
        if (duplicatedName) {
            throw ValidationUtils.badRequest("Nha cung cap da ton tai");
        }

        boolean duplicatedPhone = currentId == null
                ? nhaCungCapRepository.existsBySdt(sdt)
                : nhaCungCapRepository.existsBySdtAndIdNot(sdt, currentId);
        if (duplicatedPhone) {
            throw ValidationUtils.badRequest("So dien thoai nha cung cap da ton tai");
        }

        nhaCungCap.setTenNCC(tenNCC);
        nhaCungCap.setSDT(sdt);
        nhaCungCap.setDiaChi(ValidationUtils.trimToNull(nhaCungCap.getDiaChi()));
    }
}
