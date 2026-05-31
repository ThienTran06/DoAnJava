package com.library.librarymanager.service.Interface;

import com.library.librarymanager.entity.Sach;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface SachService {

        List<Sach> getAll();

        Sach getById(int id);

        Sach create(
                String tenSach,
                BigDecimal giaBan,
                Integer soLuongTon,
                Integer namXuatBan,
                Integer theLoaiId,
                Integer nhaXuatBanId,
                List<Integer> tacGiaIds,
                MultipartFile hinhAnh
        );

        Sach updateById(
                int id,
                String tenSach,
                BigDecimal giaBan,
                Integer soLuongTon,
                Integer namXuatBan,
                Integer theLoaiId,
                Integer nhaXuatBanId,
                List<Integer> tacGiaIds,
                MultipartFile hinhAnh
        );

        void deleteById(int id);

        List<Sach> search(
                String tenSach,
                String tenTheLoai,
                String tenTacGia,
                Integer namXuatBan
        );
        Page<Sach> getDanhSachSach(
                String keyword,
                int page,
                int size
        );
        public Page<Sach> getTatCaSach(
                String keyword,
                int page,
                int size
        );
}