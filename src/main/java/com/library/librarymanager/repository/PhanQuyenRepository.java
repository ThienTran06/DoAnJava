package com.library.librarymanager.repository;


import com.library.librarymanager.entity.NguoiDung;
import com.library.librarymanager.entity.PhanQuyen;
import com.library.librarymanager.entity.PhanQuyenId;
import com.library.librarymanager.entity.ChucNang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhanQuyenRepository
        extends JpaRepository<PhanQuyen, PhanQuyenId> {
    boolean existsByNguoiDungAndChucNang(NguoiDung nguoiDung, ChucNang chucNang);

    void deleteAllByNguoiDung(NguoiDung nguoiDung);
    void deleteByNguoiDungAndChucNang(NguoiDung nguoiDung,ChucNang chucNang);
}
