package com.library.librarymanager.service.Interface;

import com.library.librarymanager.dto.response.SachTonKhoResponse;
import com.library.librarymanager.entity.Sach;

import java.util.List;

public interface SachService {
        List<Sach> getAll();
        Sach getById(int id);
        Sach create(Sach sach);
        Sach updateById(int id,Sach sach);
        void deleteById(int id);
        List<Sach> search(String tenSach, String tenTheLoai, String tenTacGia, Integer namXuatBan);
        List<SachTonKhoResponse> getStockByName(String tenSach);
         List<SachTonKhoResponse> getTonKhoNhieuNhat();
         Integer getTongSoLuongTon();
         List<SachTonKhoResponse> getTonKhoIt();
}
