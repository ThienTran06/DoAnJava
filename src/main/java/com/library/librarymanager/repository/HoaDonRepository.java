package com.library.librarymanager.repository;

import com.library.librarymanager.dto.response.DoanhThuNgayResponse;
import com.library.librarymanager.dto.response.DoanhThuThangResponse;
import com.library.librarymanager.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon,Integer> {
    @Query("SELECT MONTH(a.ngayBan) as thang , SUM(a.tongTien) as tongTien "
    + "FROM HoaDon a "
    + "WHERE YEAR(a.ngayBan) = :nam AND a.trangThai = 'HOAN THANH' "
    + "GROUP BY MONTH(a.ngayBan) "
    + "ORDER BY MONTH(a.ngayBan)")
    List<DoanhThuThangResponse> doanhThuTheoThang(@Param("nam") int nam);

    @Query("SELECT DAY(a.ngayBan) as ngay , SUM(a.tongTien) as tongTien "
            + "FROM HoaDon a "
            + "WHERE YEAR(a.ngayBan) = :nam AND MONTH(a.ngayBan) = :thang AND a.trangThai = 'HOAN THANH' "
            + "GROUP BY DAY(a.ngayBan) "
            + "ORDER BY DAY(a.ngayBan)")
    List<DoanhThuNgayResponse> doanhThuTheoNgay(@Param("nam") int nam, @Param("thang") int thang);
}
