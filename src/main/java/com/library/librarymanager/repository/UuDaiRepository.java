package com.library.librarymanager.repository;

import com.library.librarymanager.entity.UuDai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UuDaiRepository extends JpaRepository<UuDai, Integer> {
    List<UuDai> findAllByOrderByNgayBatDauDescIdDesc();

    @Query("""
        SELECT u
        FROM UuDai u
        JOIN u.danhSachSach s
        WHERE s.id = :sachId
          AND u.trangThai = true
          AND u.ngayBatDau <= :ngay
          AND u.ngayKetThuc >= :ngay
        ORDER BY u.phanTramGiam DESC, u.id DESC
    """)
    List<UuDai> findActiveBySachId(
            @Param("sachId") int sachId,
            @Param("ngay") LocalDate ngay
    );
}
