package com.library.librarymanager.repository;

import com.library.librarymanager.entity.PhieuNhap;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PhieuNhapRepository extends JpaRepository<PhieuNhap,Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PhieuNhap p WHERE p.id = :id")
    Optional<PhieuNhap> findByIdForUpdate(@Param("id") int id);
}
