package com.library.librarymanager.service.Interface;

import com.library.librarymanager.dto.request.UuDaiRequest;
import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.entity.UuDai;

import java.math.BigDecimal;
import java.util.List;

public interface UuDaiService {
    List<UuDai> getAll();

    UuDai create(UuDaiRequest request);

    UuDai update(int id, UuDaiRequest request);

    void delete(int id);

    UuDai getActiveForSach(int sachId);

    BigDecimal tinhGiaSauUuDai(Sach sach);

    void ganUuDaiHienTai(Sach sach);
}
