package com.library.librarymanager.repository;

import com.library.librarymanager.entity.MaGiamGia;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaGiamGiaRepository extends JpaRepository<MaGiamGia, Integer> {
    Optional<MaGiamGia> findByMaIgnoreCase(String ma);

    List<MaGiamGia> findAllByOrderByNgayTaoDesc();

    List<MaGiamGia> findByKhachHangIdOrderByNgayTaoDesc(int khachHangId);

    boolean existsByMa(String ma);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM MaGiamGia m WHERE UPPER(m.ma) = UPPER(:ma)")
    Optional<MaGiamGia> findByMaForUpdate(@Param("ma") String ma);
}
