package com.library.librarymanager.entity;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private String hoTen;
    private String username;
    private String password;
    private String SDT;
    private String email;
    private String avatar;
    private String caLamViec;
    private String diaChi;
    private String ngayVaoLam;
    private Long luongCoBan;
    private String ghiChu;

    @ManyToOne
    @JoinColumn(name = "tenNhom")
    private NhomNguoiDung nhom;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<PhanQuyen> dsPhanQuyen;
}