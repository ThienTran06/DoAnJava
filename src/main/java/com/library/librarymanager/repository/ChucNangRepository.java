package com.library.librarymanager.repository;


import com.library.librarymanager.entity.ChucNang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChucNangRepository
        extends JpaRepository<ChucNang, Integer> {
    List<ChucNang> findByTenChucNangStartingWith(String prefix);

    List<ChucNang> findByTenChucNangIn(List<String> tenChucNang);

}
