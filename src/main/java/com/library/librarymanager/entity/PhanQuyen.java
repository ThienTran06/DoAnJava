package com.library.librarymanager.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "phan_quyen")
@IdClass(PhanQuyenId.class)
public class PhanQuyen {

    @Id
    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung", referencedColumnName = "id")
    private NguoiDung nguoiDung;

    @Id
    @ManyToOne
    @JoinColumn(name = "ma_chuc_nang", referencedColumnName = "ma_chuc_nang")
    private ChucNang chucNang;


}