package com.library.librarymanager.repository;


import com.library.librarymanager.entity.NguoiDung;
import com.library.librarymanager.entity.PhanQuyen;
import com.library.librarymanager.entity.PhanQuyenId;
import com.library.librarymanager.entity.ChucNang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhanQuyenRepository
        extends JpaRepository<PhanQuyen, PhanQuyenId> {
    @Query("""
    select count(p) > 0
    from PhanQuyen p
    where p.nguoiDung = :nguoiDung
      and p.chucNang = :chucNang
""")
    boolean checkExist(
            @Param("nguoiDung") NguoiDung nguoiDung,
            @Param("chucNang") ChucNang chucNang
    );

    void deleteAllByNguoiDung(NguoiDung nguoiDung);
    void deleteByNguoiDungAndChucNang(NguoiDung nguoiDung,ChucNang chucNang);
}
