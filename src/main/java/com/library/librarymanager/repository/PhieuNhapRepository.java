package com.library.librarymanager.repository;

import com.library.librarymanager.entity.PhieuNhap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface PhieuNhapRepository extends JpaRepository<PhieuNhap,Integer> {
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
            Integer id,
            LocalDateTime tuNgay,
            LocalDateTime denNgay,
            Pageable pageable
    );
    @Query("""
    select
        count(pn),
        sum(case when date(pn.ngayNhap) = current_date then 1 else 0 end),
        sum(case when date(pn.ngayNhap) = current_date then pn.tongTien else 0 end),
        sum(pn.tongTien)
    from PhieuNhap pn
""")
    Object getThongKePhieuNhap();
}
