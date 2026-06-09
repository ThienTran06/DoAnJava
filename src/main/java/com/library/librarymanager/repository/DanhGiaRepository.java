package com.library.librarymanager.repository;

import com.library.librarymanager.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
    List<DanhGia> findByTrangThaiNot(String trangThai);
    List<DanhGia> findByTrangThai(String trangThai);
}
