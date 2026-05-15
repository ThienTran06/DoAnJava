package com.library.librarymanager.repository;


import com.library.librarymanager.entity.PhieuDatGiuSach;

import com.library.librarymanager.enums.TrangThaiGiu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PhieuDatGiuSachRepository extends JpaRepository<PhieuDatGiuSach, Integer> {
    List<PhieuDatGiuSach> findByTrangThaiAndExpiredAtBefore(
            TrangThaiGiu trangThai,
            LocalDateTime time
    );
    boolean existsByKhachHangIdAndTrangThai(int khachHangId, TrangThaiGiu tt);
}