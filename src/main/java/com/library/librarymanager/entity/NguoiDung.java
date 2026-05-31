package com.library.librarymanager.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    @JsonIgnore
    private String password;
    private String SDT;
    @Email
    private String email;
    private String avatar;
    private String caLamViec;
    private String diaChi;
    private LocalDateTime ngayVaoLam;
    private BigDecimal luongCoBan;
    private String ghiChu;
    @ManyToOne
    @JoinColumn(name = "tenNhom")
    private NhomNguoiDung nhom;
    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<PhanQuyen> dsPhanQuyen;
}
