package com.library.librarymanager.repository;

import com.library.librarymanager.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {
    @Query("""
        SELECT k
        FROM KhachHang k
        WHERE
            :keyword IS NULL
            OR :keyword = ''
            OR LOWER(k.hoTen)
                LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR k.sdt
                LIKE CONCAT('%', :keyword, '%')
    """)
    Page<KhachHang> search(
            @Param("keyword") String keyword,
            Pageable pageable);
}
