package com.library.librarymanager.repository;


import com.library.librarymanager.entity.NhomNguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NhomNguoiDungRepository extends JpaRepository<NhomNguoiDung, String> {
    Optional<NhomNguoiDung> findByTenNhom(String tenNhom);

}
