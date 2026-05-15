package com.library.librarymanager.repository;

import com.library.librarymanager.entity.ChiTietPhieuGiu;

import com.library.librarymanager.enums.TrangThaiGiu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChiTietPhieuGiuRepository extends JpaRepository<ChiTietPhieuGiu, Integer> {

    List<ChiTietPhieuGiu> findByPhieuGiuId(int phieuId);

    boolean existsBySach_IdAndPhieuGiu_IdNotAndPhieuGiu_TrangThai(
            int sachId,
            int phieuId,
            TrangThaiGiu trangThai
    );
}