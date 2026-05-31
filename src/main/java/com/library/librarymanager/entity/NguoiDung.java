package com.library.librarymanager.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "nguoi_dung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ho_ten")
    private String hoTen;

    private String username;
    private String password;

    @Column(name = "sdt")
    private String SDT;

    private String email;
    private String avatar;

    @Column(name = "ca_lam_viec")
    private String caLamViec;

    @Column(name = "chuc_vu")
    private String chucVu;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "ngay_vao_lam")
    private String ngayVaoLam;

    @Column(name = "luong_co_ban")
    private Long luongCoBan;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @ManyToOne
    @JoinColumn(name = "ten_nhom", referencedColumnName = "id")
    private NhomNguoiDung nhom;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<PhanQuyen> dsPhanQuyen;
}