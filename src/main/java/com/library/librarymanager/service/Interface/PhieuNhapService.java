package com.library.librarymanager.service.Interface;

import com.library.librarymanager.dto.request.PhieuNhapRequest;
import com.library.librarymanager.dto.request.UpdatePhieuNhapRequest;
import com.library.librarymanager.dto.response.ThongKePhieuNhapResponse;
import com.library.librarymanager.entity.PhieuNhap;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface PhieuNhapService {
    List<PhieuNhap> getAll();
    Page<PhieuNhap> getAll(Integer id, LocalDate ngay, int page, int size);
    PhieuNhap getById(int id);
    PhieuNhap create(PhieuNhapRequest phieuNhap);
    PhieuNhap updateById(int id, UpdatePhieuNhapRequest request);
    PhieuNhap updateChiTiet(int id, PhieuNhapRequest request);
    void huyPhieuNhap(int id);
    void deleteById(int id);
    ThongKePhieuNhapResponse getThongKe();
}
