package com.library.librarymanager.repository;

import com.library.librarymanager.entity.NhaCungCap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NhaCungCapRepository extends JpaRepository<NhaCungCap,Integer> {
    boolean existsByTenNCCIgnoreCase(String tenNCC);

    boolean existsByTenNCCIgnoreCaseAndIdNot(String tenNCC, int id);

    @Query("""
        SELECT COUNT(n) > 0
        FROM NhaCungCap n
        WHERE n.SDT = :sdt
    """)
    boolean existsBySdt(@Param("sdt") String sdt);

    @Query("""
        SELECT COUNT(n) > 0
        FROM NhaCungCap n
        WHERE n.SDT = :sdt
        AND n.id <> :id
    """)
    boolean existsBySdtAndIdNot(@Param("sdt") String sdt, @Param("id") int id);

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
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
