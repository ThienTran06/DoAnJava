package com.library.librarymanager.repository;


import com.library.librarymanager.entity.NhomNguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NhomNguoiDungRepository extends JpaRepository<NhomNguoiDung, Integer> {
    @Query(value = "SELECT * FROM nhom_nguoi_dung WHERE ten_nhom = :tenNhom LIMIT 1", nativeQuery = true)
    Optional<NhomNguoiDung> findByTenNhom(@Param("tenNhom") String tenNhom);

}
