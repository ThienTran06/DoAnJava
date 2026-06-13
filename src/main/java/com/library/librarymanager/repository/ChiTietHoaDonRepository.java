package com.library.librarymanager.repository;

import com.library.librarymanager.dto.response.DoanhThuTheoTheLoaiResponse;
import com.library.librarymanager.dto.response.SachBanChayResponse;
import com.library.librarymanager.dto.response.TopSachDoanhThuResponse;
import com.library.librarymanager.entity.ChiTietHoaDon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    // Kiểm tra khách hàng đã mua sách cụ thể
    @Query("SELECT COUNT(ct) > 0 FROM ChiTietHoaDon ct "
            + "JOIN ct.hoaDon hd "
            + "WHERE hd.khachHang.id = :khachHangId "
            + "AND ct.sach.id = :sachId "
            + "AND hd.trangThai IN ('PAID','HOAN THANH')")
    boolean hasCustomerBoughtBook(@Param("khachHangId") int khachHangId, @Param("sachId") int sachId);

    // Kiểm tra khách hàng có đơn hàng nào (là khách hàng thực)
    @Query("SELECT COUNT(hd) > 0 FROM HoaDon hd "
            + "WHERE hd.khachHang.id = :khachHangId "
            + "AND hd.trangThai IN ('PAID','HOAN THANH')")
    boolean hasCustomerAnyOrder(@Param("khachHangId") int khachHangId);
}
