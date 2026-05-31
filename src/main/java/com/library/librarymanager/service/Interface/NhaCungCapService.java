package com.library.librarymanager.service.Interface;

import com.library.librarymanager.entity.NhaCungCap;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NhaCungCapService {
    Page<NhaCungCap> getDanhSachNhaCungCap(
            String keyword,
            int page,
            int size
    );
    NhaCungCap getById(int id);
    NhaCungCap create(NhaCungCap nhaCungCap);
    NhaCungCap updateById(int id,NhaCungCap nhaCungCap);
    void deleteById(int id);
}
