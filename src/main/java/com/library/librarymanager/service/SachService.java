package com.library.librarymanager.service;

import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.entity.TacGia;

import java.util.List;

public interface SachService {
        List<Sach> getAll();
        Sach getById(int id);
        Sach create(Sach sach);
        Sach updateById(int id,Sach sach);
        void deleteById(int id);
        List<Sach> search(String tenSach, String tenTheLoai, String tenTacGia, Integer namXuatBan);
}
