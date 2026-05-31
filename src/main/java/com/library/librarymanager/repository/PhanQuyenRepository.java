package com.library.librarymanager.repository;


import com.library.librarymanager.entity.PhanQuyen;
import com.library.librarymanager.entity.PhanQuyenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhanQuyenRepository
        extends JpaRepository<PhanQuyen, PhanQuyenId> {
    @Query(value = """
            select count(*)
            from phan_quyen
            where ma_nguoi_dung = :nguoiDungId
              and ma_chuc_nang = :chucNangId
            """, nativeQuery = true)
    long countByNguoiDungIdAndChucNangId(@Param("nguoiDungId") int nguoiDungId,
                                         @Param("chucNangId") int chucNangId);

    @Modifying
    @Query(value = "delete from phan_quyen where ma_nguoi_dung = :nguoiDungId", nativeQuery = true)
    void deleteAllByNguoiDungId(@Param("nguoiDungId") int nguoiDungId);
}
