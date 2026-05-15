package com.library.librarymanager.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private String password;
    private String SDT;


    @ManyToOne
    @JoinColumn(name = "tenNhom")
    private NhomNguoiDung nhom;
    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<PhanQuyen> dsPhanQuyen;
}
