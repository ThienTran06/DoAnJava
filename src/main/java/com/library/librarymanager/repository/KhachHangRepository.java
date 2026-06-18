package com.library.librarymanager.repository;

import com.library.librarymanager.entity.KhachHang;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {
    @Query("""
        SELECT COUNT(k) > 0
        FROM KhachHang k
        WHERE k.SDT = :sdt
    """)
    boolean existsBySdt(@Param("sdt") String sdt);

    @Query("""
        SELECT COUNT(k) > 0
        FROM KhachHang k
        WHERE k.SDT = :sdt
        AND k.id <> :id
    """)
    boolean existsBySdtAndIdNot(@Param("sdt") String sdt, @Param("id") int id);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, int id);

    Optional<KhachHang> findByEmailIgnoreCase(String email);

    @Query("""
        SELECT k
        FROM KhachHang k
        WHERE
            :keyword IS NULL
            OR :keyword = ''
            OR LOWER(k.hoTen)
                LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR k.SDT
                LIKE CONCAT('%', :keyword, '%')
    """)
    Page<KhachHang> search(
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("""
        SELECT k
        FROM KhachHang k
        WHERE k.SDT = :sdt
    """)
    Optional<KhachHang> findBySDT(@Param("sdt") String sdt);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT k FROM KhachHang k WHERE k.id = :id")
    Optional<KhachHang> findByIdForUpdate(@Param("id") int id);
}
