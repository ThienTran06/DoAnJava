package com.library.librarymanager.repository;

import com.library.librarymanager.dto.response.SachTonKhoResponse;
import com.library.librarymanager.entity.Sach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SachRepository extends JpaRepository<Sach,Integer> {
    @Query("SELECT s.tenSach as tenSach,s.soLuongTon as soLuongTonKho "
    +"FROM Sach s "
    +"ORDER BY s.soLuongTon DESC ")
    List<SachTonKhoResponse> tonKhoNhieuNhat();
    @Query("SELECT s FROM Sach s LEFT JOIN s.danhSachTacGia tg WHERE " +
            "(:tenSach IS NULL OR s.tenSach LIKE %:tenSach%) AND " +
            "(:tenTheLoai IS NULL OR s.theLoai.tenTheLoai LIKE %:tenTheLoai%) AND " +
            "(:tenTacGia IS NULL OR tg.hoTen LIKE %:tenTacGia%) AND " +
            "(:namXuatBan IS NULL OR s.namXuatBan = :namXuatBan)")
    List<Sach> search(
            @Param("tenSach") String tenSach,
            @Param("tenTheLoai") String tenTheLoai,
            @Param("tenTacGia") String tenTacGia,
            @Param("namXuatBan") Integer namXuatBan
    );
}
