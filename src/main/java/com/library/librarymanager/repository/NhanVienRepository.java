package com.library.librarymanager.repository;

import com.library.librarymanager.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NhanVienRepository extends JpaRepository<NhanVien,Integer> {
    Optional<NhanVien> findByUsername(String username);
}
