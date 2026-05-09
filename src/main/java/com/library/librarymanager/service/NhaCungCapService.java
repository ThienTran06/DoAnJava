package com.library.librarymanager.service;

import com.library.librarymanager.entity.NhaCungCap;
import com.library.librarymanager.entity.Sach;

import java.util.List;

public interface NhaCungCapService {
    List<NhaCungCap> getAll();
    NhaCungCap getById(int id);
    NhaCungCap create(NhaCungCap nhaCungCap);
    NhaCungCap updateById(int id,NhaCungCap nhaCungCap);
    void deleteById(int id);
}
