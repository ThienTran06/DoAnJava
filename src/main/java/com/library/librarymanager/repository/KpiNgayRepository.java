package com.library.librarymanager.repository;

import com.library.librarymanager.entity.KpiNgay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface KpiNgayRepository extends JpaRepository<KpiNgay, Integer> {
    Optional<KpiNgay> findByNgay(LocalDate ngay);
}
