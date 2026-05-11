package com.library.librarymanager.entity;


import jakarta.persistence.*;
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
    @JoinColumn(name = "ma_nguoi_dung")
    private NguoiDung nguoiDung;

    @Id
    @ManyToOne
    @JoinColumn(name = "ma_chuc_nang")
    private ChucNang chucNang;


}