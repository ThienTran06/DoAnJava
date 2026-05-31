package com.library.librarymanager.service.Interface;

import com.library.librarymanager.dto.request.PhieuNhapRequest;
import com.library.librarymanager.dto.response.ThongKePhieuNhapResponse;
import com.library.librarymanager.entity.PhieuNhap;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface PhieuNhapService {
     Page<PhieuNhap> getAll(Integer ma, LocalDate ngay, int page, int size);
    PhieuNhap getById(int id);
    PhieuNhap create(PhieuNhapRequest phieuNhap);
    PhieuNhap updateById(int id,PhieuNhap phieuNhap);
    void deleteById(int id);
    public ThongKePhieuNhapResponse getThongKe();
}
