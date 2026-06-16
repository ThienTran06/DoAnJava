package com.library.librarymanager.repository;


import com.library.librarymanager.entity.PhieuDatGiuSach;

import com.library.librarymanager.enums.TrangThaiGiu;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PhieuDatGiuSachRepository extends JpaRepository<PhieuDatGiuSach, Integer> {
    List<PhieuDatGiuSach> findByTrangThaiAndExpiredAtBefore(
            TrangThaiGiu trangThai,
            LocalDateTime time
    );
    boolean existsByKhachHangIdAndTrangThai(int khachHangId, TrangThaiGiu tt);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PhieuDatGiuSach p WHERE p.id = :id")
    Optional<PhieuDatGiuSach> findByIdForUpdate(@Param("id") int id);
    @Query("""
    SELECT p
    FROM PhieuDatGiuSach p
    WHERE (:id IS NULL OR p.id = :id)
      AND (:tuNgay IS NULL OR p.createdAt >= :tuNgay)
      AND (:denNgay IS NULL OR p.createdAt < :denNgay)
""")
    Page<PhieuDatGiuSach> getAll(
            @Param("id") Integer id,
            @Param("tuNgay") LocalDateTime tuNgay,
            @Param("denNgay") LocalDateTime denNgay,
            Pageable pageable
    );
}
