package com.library.librarymanager.repository;

import com.library.librarymanager.entity.PhieuNhap;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PhieuNhapRepository extends JpaRepository<PhieuNhap,Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PhieuNhap p WHERE p.id = :id")
    Optional<PhieuNhap> findByIdForUpdate(@Param("id") int id);
    @Query("""
    SELECT p
    FROM PhieuNhap p
    WHERE (:id IS NULL OR p.id = :id)
      AND (
            :tuNgay IS NULL
            OR (
                p.ngayNhap >= :tuNgay
                AND p.ngayNhap < :denNgay
            )
          )
""")
    Page<PhieuNhap> getAll(
            @Param("id") Integer id,
            @Param("tuNgay") LocalDateTime tuNgay,
            @Param("denNgay") LocalDateTime denNgay,
            Pageable pageable
    );
    @Query("""
    select
        count(pn),
        sum(case when pn.ngayNhap >= :startOfDay and pn.ngayNhap < :startOfNextDay then 1 else 0 end),
        sum(case when pn.ngayNhap >= :startOfDay and pn.ngayNhap < :startOfNextDay then pn.tongTien else 0 end),
        sum(pn.tongTien)
    from PhieuNhap pn
    where pn.trangThai <> 'DA HUY'
""")
    Object getThongKePhieuNhap(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("startOfNextDay") LocalDateTime startOfNextDay
    );
}
