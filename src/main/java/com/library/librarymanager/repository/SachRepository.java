package com.library.librarymanager.repository;

import com.library.librarymanager.dto.response.SachTonKhoResponse;
import com.library.librarymanager.entity.Sach;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SachRepository extends JpaRepository<Sach,Integer> {
    @Query("SELECT s.tenSach as tenSach,s.soLuongTon as soLuongTonKho "
    +"FROM Sach s "
    +"ORDER BY s.soLuongTon DESC ")
    List<SachTonKhoResponse> tonKhoNhieuNhat();
    @Query("SELECT s FROM Sach s LEFT JOIN s.danhSachTacGia tg WHERE " +
            "(:tenSach IS NULL OR s.tenSach LIKE %:tenSach%) AND " +
            "(:tenTheLoai IS NULL OR s.theLoai.tenTheLoai LIKE %:tenTheLoai%) AND " +
            "(:tenTacGia IS NULL OR tg.hoTen LIKE %:tenTacGia%) AND " +
            "(:namXuatBan IS NULL OR s.namXuatBan = :namXuatBan)")
    List<Sach> search(
            @Param("tenSach") String tenSach,
            @Param("tenTheLoai") String tenTheLoai,
            @Param("tenTacGia") String tenTacGia,
            @Param("namXuatBan") Integer namXuatBan
    );

    @Query("SELECT s.tenSach as tenSach,s.soLuongTon as soLuongTonKho "
    + "FROM Sach s "
    + "WHERE (:tenSach IS NULL OR s.tenSach LIKE %:tenSach%) ")
    List<SachTonKhoResponse> tonKhoTheoTen(@Param("tenSach")String tenSach);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s from Sach s
        where s.id = :id
    """)
    Optional<Sach> findByIdForUpdate(int id);
    @Query("""
    SELECT s
    FROM Sach s
    WHERE s.soLuongTon > 0
    AND (
        :keyword IS NULL
        OR :keyword = ''
        OR LOWER(s.tenSach)
           LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
""")
    Page<Sach> search(
            @Param("keyword") String keyword,
            Pageable pageable
    );
    @Query("""
    SELECT s
    FROM Sach s
    WHERE (
        :keyword IS NULL
        OR :keyword = ''
        OR LOWER(s.tenSach)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
""")
    Page<Sach> searchAll(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
