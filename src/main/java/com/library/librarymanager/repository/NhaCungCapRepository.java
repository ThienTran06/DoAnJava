package com.library.librarymanager.repository;

import com.library.librarymanager.entity.NhaCungCap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NhaCungCapRepository extends JpaRepository<NhaCungCap,Integer> {
    @Query("""
    SELECT n
    FROM NhaCungCap n
    WHERE (
        :keyword IS NULL
        OR :keyword = ''
        OR CAST(n.id AS string) LIKE %:keyword%
        OR LOWER(n.tenNCC) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(n.diaChi) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR n.SDT LIKE CONCAT('%', :keyword, '%')
    )
""")
    Page<NhaCungCap> getDanhSachNhaCungCap(
            String keyword,
            Pageable pageable
    );
}
