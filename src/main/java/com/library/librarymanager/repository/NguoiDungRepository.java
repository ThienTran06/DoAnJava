package com.library.librarymanager.repository;

import com.library.librarymanager.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NguoiDungRepository extends JpaRepository<NguoiDung,Integer> {
    Optional<NguoiDung> findByUsername(String username);
}
