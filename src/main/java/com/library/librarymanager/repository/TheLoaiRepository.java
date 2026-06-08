package com.library.librarymanager.repository;

import com.library.librarymanager.entity.TheLoai;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheLoaiRepository extends JpaRepository<TheLoai,Integer> {
    boolean existsByTenTheLoaiIgnoreCase(String tenTheLoai);

    boolean existsByTenTheLoaiIgnoreCaseAndIdNot(String tenTheLoai, int id);
}
