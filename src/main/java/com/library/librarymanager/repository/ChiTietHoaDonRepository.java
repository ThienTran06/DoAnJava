package com.library.librarymanager.repository;

import com.library.librarymanager.dto.response.SachBanChayResponse;
import com.library.librarymanager.entity.ChiTietHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon,Integer> {
    @Query("SELECT ct.sach.tenSach as tenSach,SUM(ct.soLuong) as soLuongDaBan "
    +"FROM ChiTietHoaDon ct "
    +"GROUP BY ct.sach.tenSach "
    +"ORDER BY SUM(ct.soLuong) DESC ")
    List<SachBanChayResponse> sachBanChay();
}
