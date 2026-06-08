package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.TacGia;
import com.library.librarymanager.repository.TacGiaRepository;
import com.library.librarymanager.service.Interface.TacGiaService;
import com.library.librarymanager.util.ValidationUtils;
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
        return tacGiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay tac gia co id = " + id));
    }

    @Override
    public TacGia create(TacGia tacGia) {
        validateTacGia(tacGia, null);
        return tacGiaRepository.save(tacGia);
    }

    @Override
    public TacGia updateById(int id, TacGia tacGia) {
        TacGia res = getById(id);
        validateTacGia(tacGia, id);
        res.setHoTen(tacGia.getHoTen());
        res.setQuocTich(tacGia.getQuocTich());
        return tacGiaRepository.save(res);
    }

    @Override
    public void deleteById(int id) {
        tacGiaRepository.deleteById(id);
    }

    private void validateTacGia(TacGia tacGia, Integer currentId) {
        if (tacGia == null) {
            throw ValidationUtils.badRequest("Du lieu tac gia khong duoc de trong");
        }
        String hoTen = ValidationUtils.requireText(tacGia.getHoTen(), "Ten tac gia");
        boolean duplicated = currentId == null
                ? tacGiaRepository.existsByHoTenIgnoreCase(hoTen)
                : tacGiaRepository.existsByHoTenIgnoreCaseAndIdNot(hoTen, currentId);
        if (duplicated) {
            throw ValidationUtils.badRequest("Tac gia da ton tai");
        }
        tacGia.setHoTen(hoTen);
        tacGia.setQuocTich(ValidationUtils.trimToNull(tacGia.getQuocTich()));
    }
}
