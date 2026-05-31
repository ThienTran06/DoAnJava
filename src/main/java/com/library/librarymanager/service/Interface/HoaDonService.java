package com.library.librarymanager.service.Interface;

import com.library.librarymanager.dto.request.HoaDonRequest;
import com.library.librarymanager.dto.response.ThongKeHoaDon;
import com.library.librarymanager.entity.HoaDon;
import org.springframework.data.domain.Page;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface HoaDonService {
     Page<HoaDon> getAll(Integer ma, LocalDate ngay, int page, int size);
    HoaDon getById(int id);
    HoaDon create(HoaDonRequest request);
    HoaDon updateById(int id,HoaDon hoaDon);
    void deleteById(int id);
    void huyHoaDon(int id);
    ThongKeHoaDon getThongKe();

}
