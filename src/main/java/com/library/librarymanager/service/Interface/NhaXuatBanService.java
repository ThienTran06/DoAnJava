package com.library.librarymanager.service.Interface;

import com.library.librarymanager.entity.NhaXuatBan;

import java.util.List;

public interface NhaXuatBanService {
    List<NhaXuatBan> getAll();
    NhaXuatBan getById(int id);
    NhaXuatBan create(NhaXuatBan nhaXuatBan);
    NhaXuatBan updateById(int id,NhaXuatBan nhaXuatBan);
    void deleteById(int id);
}
