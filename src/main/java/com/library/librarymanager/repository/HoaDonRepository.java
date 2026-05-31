package com.library.librarymanager.repository;

import com.library.librarymanager.dto.response.DoanhThuNamResponse;
import com.library.librarymanager.dto.response.DoanhThuNgayResponse;
import com.library.librarymanager.dto.response.DoanhThuThangResponse;
import com.library.librarymanager.entity.HoaDon;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


    @Query("SELECT MONTH(a.ngayBan) as thang , SUM(a.tongTien) as tongTien "
    + "FROM HoaDon a "
    + "WHERE YEAR(a.ngayBan) = :nam AND a.trangThai IN ('PAID','HOAN THANH') "
    + "GROUP BY MONTH(a.ngayBan) "
    + "ORDER BY MONTH(a.ngayBan)")
    List<DoanhThuThangResponse> doanhThuTheoThang(@Param("nam") int nam);

    @Query("SELECT DAY(a.ngayBan) as ngay , SUM(a.tongTien) as tongTien "
            + "FROM HoaDon a "
            + "WHERE YEAR(a.ngayBan) = :nam AND MONTH(a.ngayBan) = :thang AND a.trangThai IN ('HOAN THANH','PAID') "
            + "GROUP BY DAY(a.ngayBan) "
            + "ORDER BY DAY(a.ngayBan)")
    List<DoanhThuNgayResponse> doanhThuTheoNgay(@Param("nam") int nam, @Param("thang") int thang);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM HoaDon h WHERE h.id = :id")
    Optional<HoaDon> findByIdForUpdate(@Param("id") int id);
    @Query("""
    SELECT h
    FROM HoaDon h
    WHERE (:id IS NULL OR h.id = :id)
      AND (
            :tuNgay IS NULL
            OR h.ngayBan >= :tuNgay
               AND h.ngayBan < :denNgay
      )
""")
    Page<HoaDon> getAll(
            @Param("id") Integer id,
            @Param("tuNgay") LocalDateTime tuNgay,
            @Param("denNgay") LocalDateTime denNgay,
            Pageable pageable
    );
    @Query("""
        select
            count(h),
            sum(case when date(h.ngayBan) = current_date then 1 else 0 end),
            sum(case when date(h.ngayBan) = current_date then h.tongTien else 0 end),
            sum(h.tongTien)
        from HoaDon h
    """)
    Object getThongKeHoaDon();
}
