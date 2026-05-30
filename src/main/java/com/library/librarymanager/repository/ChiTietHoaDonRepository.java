package com.library.librarymanager.repository;

import com.library.librarymanager.dto.response.DoanhThuTheoTheLoaiResponse;
import com.library.librarymanager.dto.response.SachBanChayResponse;
import com.library.librarymanager.dto.response.TopSachDoanhThuResponse;
import com.library.librarymanager.entity.ChiTietHoaDon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon,Integer> {
    @Query("SELECT ct.sach.tenSach as tenSach,SUM(ct.soLuong) as soLuongDaBan "
    +"FROM ChiTietHoaDon ct "
    +"GROUP BY ct.sach.tenSach "
    +"ORDER BY SUM(ct.soLuong) DESC ")
    List<SachBanChayResponse> sachBanChay();
    @Query("SELECT s.theLoai.tenTheLoai AS tenTheLoai, "
            + "SUM(ct.thanhTien) AS tongTien "
            + "FROM ChiTietHoaDon ct "
            + "JOIN ct.hoaDon hd "
            + "JOIN ct.sach s "
            + "WHERE hd.trangThai IN ('PAID','HOAN THANH') "
            + "GROUP BY s.theLoai.tenTheLoai "
            + "ORDER BY SUM(ct.thanhTien) DESC")
    List<DoanhThuTheoTheLoaiResponse> doanhThuTheoTheLoai();

    @Query("SELECT s.id AS sachId, "
            + "s.tenSach AS tenSach, "
            + "SUM(ct.soLuong) AS soLuongDaBan, "
            + "SUM(ct.thanhTien) AS tongDoanhThu "
            + "FROM ChiTietHoaDon ct "
            + "JOIN ct.hoaDon hd "
            + "JOIN ct.sach s "
            + "WHERE hd.trangThai IN ('PAID','HOAN THANH') "
            + "GROUP BY s.id, s.tenSach "
            + "ORDER BY SUM(ct.thanhTien) DESC")
    List<TopSachDoanhThuResponse> sachTopDoanhThu(Pageable pageable);
}
