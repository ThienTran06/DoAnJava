package com.library.librarymanager.repository;

import com.library.librarymanager.dto.response.DoanhThuNamResponse;
import com.library.librarymanager.dto.response.DoanhThuNgayResponse;
import com.library.librarymanager.dto.response.DoanhThuThangResponse;
import com.library.librarymanager.entity.HoaDon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HoaDonRepository extends JpaRepository<HoaDon,Integer> {
    @Query("SELECT YEAR(a.ngayBan) as nam , SUM(a.tongTien) as tongTien "
            + "FROM HoaDon a "
            + "WHERE a.trangThai IN ('PAID','HOAN THANH') "
            + "GROUP BY YEAR(a.ngayBan) "
            + "ORDER BY YEAR(a.ngayBan)")
    List<DoanhThuNamResponse> doanhThuTheoNam();


    @Query("SELECT MONTH(a.ngayBan) as thang, YEAR(a.ngayBan) as nam, SUM(a.tongTien) as tongTien "
    + "FROM HoaDon a "
    + "WHERE YEAR(a.ngayBan) = :nam AND a.trangThai IN ('PAID','HOAN THANH') "
    + "GROUP BY YEAR(a.ngayBan), MONTH(a.ngayBan) "
    + "ORDER BY MONTH(a.ngayBan)")
    List<DoanhThuThangResponse> doanhThuTheoThang(@Param("nam") int nam);

    @Query("SELECT DAY(a.ngayBan) as ngay, MONTH(a.ngayBan) as thang, YEAR(a.ngayBan) as nam, SUM(a.tongTien) as tongTien "
            + "FROM HoaDon a "
            + "WHERE YEAR(a.ngayBan) = :nam AND MONTH(a.ngayBan) = :thang AND a.trangThai IN ('HOAN THANH','PAID') "
            + "GROUP BY YEAR(a.ngayBan), MONTH(a.ngayBan), DAY(a.ngayBan) "
            + "ORDER BY DAY(a.ngayBan)")
    List<DoanhThuNgayResponse> doanhThuTheoNgay(@Param("nam") int nam, @Param("thang") int thang);
    @Query("SELECT SUM(a.tongTien) "
            + "FROM HoaDon a "
            + "WHERE a.ngayBan >= :startOfDay AND a.ngayBan < :startOfNextDay "
            + "AND a.trangThai IN ('HOAN THANH','PAID')")
    BigDecimal doanhThuHomNay(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("startOfNextDay") LocalDateTime startOfNextDay
    );

    @Query("SELECT DAY(a.ngayBan) as ngay, MONTH(a.ngayBan) as thang, YEAR(a.ngayBan) as nam, SUM(a.tongTien) as tongTien " +
            "FROM HoaDon a " +
            "WHERE a.ngayBan >= :fromDate " +
            "AND a.trangThai IN ('HOAN THANH','PAID') "+
            "GROUP BY YEAR(a.ngayBan), MONTH(a.ngayBan), DAY(a.ngayBan) "+
            "ORDER BY YEAR(a.ngayBan), MONTH(a.ngayBan), DAY(a.ngayBan) "
    )
    List<DoanhThuNgayResponse> doanhThu7Ngay(@Param("fromDate") LocalDateTime fromDate);

    @Query("SELECT DAY(a.ngayBan) as ngay, MONTH(a.ngayBan) as thang, YEAR(a.ngayBan) as nam, SUM(a.tongTien) as tongTien "
            +"FROM HoaDon a "
            + "WHERE a.ngayBan >= :fromDate "
            + "AND a.trangThai IN ('HOAN THANH','PAID') "
            + "GROUP BY YEAR(a.ngayBan), MONTH(a.ngayBan), DAY(a.ngayBan) "
            + "ORDER BY YEAR(a.ngayBan), MONTH(a.ngayBan), DAY(a.ngayBan) ")
    List<DoanhThuNgayResponse> doanhThuBaMuoiNgayTruoc(@Param("fromDate") LocalDateTime fromDate);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM HoaDon h WHERE h.id = :id")
    Optional<HoaDon> findByIdForUpdate(@Param("id") int id);

    @Query("SELECT DAY(a.ngayBan) as ngay, MONTH(a.ngayBan) as thang, YEAR(a.ngayBan) as nam, SUM(a.tongTien) as tongTien "
        +"FROM HoaDon a "
        +"WHERE a.ngayBan >= :start "
        +"AND a.ngayBan < :end "
        +"AND a.trangThai IN ('HOAN THANH', 'PAID') "
            + "GROUP BY YEAR(a.ngayBan), MONTH(a.ngayBan), DAY(a.ngayBan) "
            + "ORDER BY YEAR(a.ngayBan), MONTH(a.ngayBan), DAY(a.ngayBan) ")
    List<DoanhThuNgayResponse> getDoanhThuKhoangNgay(@Param("start") LocalDateTime tuNgay, @Param("end") LocalDateTime denNgay);
    @Query("SELECT SUM(a.tongTien) as tongTien "
        +"From HoaDon a "
        +"WHERE a.trangThai IN ('HOAN THANH', 'PAID') "
    )
    BigDecimal getTongDoanhThu();
}
