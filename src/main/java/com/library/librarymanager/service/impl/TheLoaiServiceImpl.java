package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.TheLoai;
import com.library.librarymanager.repository.TheLoaiRepository;
import com.library.librarymanager.service.Interface.TheLoaiService;
import com.library.librarymanager.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
        return theLoaiRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay the loai co id = " + id));
    }

    @Override
    public TheLoai create(TheLoai theLoai) {
        validateTheLoai(theLoai, null);
        return theLoaiRepository.save(theLoai);
    }

    @Override
    public TheLoai updateById(int id, TheLoai theLoai) {
        TheLoai res = getById(id);
        validateTheLoai(theLoai, id);
        res.setTenTheLoai(theLoai.getTenTheLoai());
        res.setMoTa(theLoai.getMoTa());
        return theLoaiRepository.save(res);
    }

    @Override
    public void deleteById(int id) {
        theLoaiRepository.deleteById(id);
    }

    private void validateTheLoai(TheLoai theLoai, Integer currentId) {
        if (theLoai == null) {
            throw ValidationUtils.badRequest("Du lieu the loai khong duoc de trong");
        }
        String tenTheLoai = ValidationUtils.requireText(theLoai.getTenTheLoai(), "Ten the loai");
        boolean duplicated = currentId == null
                ? theLoaiRepository.existsByTenTheLoaiIgnoreCase(tenTheLoai)
                : theLoaiRepository.existsByTenTheLoaiIgnoreCaseAndIdNot(tenTheLoai, currentId);
        if (duplicated) {
            throw ValidationUtils.badRequest("The loai da ton tai");
        }
        theLoai.setTenTheLoai(tenTheLoai);
        theLoai.setMoTa(ValidationUtils.trimToNull(theLoai.getMoTa()));
    }
}
