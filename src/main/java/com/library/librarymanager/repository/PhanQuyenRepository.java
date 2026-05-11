package com.library.librarymanager.repository;


import com.library.librarymanager.entity.NguoiDung;
import com.library.librarymanager.entity.PhanQuyen;
import com.library.librarymanager.entity.PhanQuyenId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhanQuyenRepository
        extends JpaRepository<PhanQuyen, PhanQuyenId> {
    void deleteAllByNguoiDung(NguoiDung nguoiDung);
}