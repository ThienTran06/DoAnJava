package com.library.librarymanager.repository;

import com.library.librarymanager.entity.TacGia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TacGiaRepository extends JpaRepository<TacGia,Integer> {
    boolean existsByHoTenIgnoreCase(String hoTen);

    boolean existsByHoTenIgnoreCaseAndIdNot(String hoTen, int id);
}
