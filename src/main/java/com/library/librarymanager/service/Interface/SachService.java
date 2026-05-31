package com.library.librarymanager.service.Interface;

import com.library.librarymanager.dto.response.SachTonKhoResponse;
import com.library.librarymanager.dto.response.SachThongKeResponse;
import com.library.librarymanager.entity.Sach;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SachService {
        List<Sach> getAll();
        Sach getById(int id);
        Sach create(Sach sach);
        Sach create(Sach sach, MultipartFile fileAnh);
        Sach updateById(int id,Sach sach);
        Sach updateById(int id, Sach sach, MultipartFile fileAnh);
        void deleteById(int id);
        List<Sach> search(String tenSach, String tenTheLoai, String tenTacGia, Integer namXuatBan);
        List<SachTonKhoResponse> getStockByName(String tenSach);
         List<SachTonKhoResponse> getTonKhoNhieuNhat();
         Integer getTongSoLuongTon();
         List<SachTonKhoResponse> getTonKhoIt();
         SachThongKeResponse getThongKeSach();
}
