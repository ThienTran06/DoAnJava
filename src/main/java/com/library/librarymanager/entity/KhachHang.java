package com.library.librarymanager.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "khach_hang")
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String hoTen;
    private String sdt;
    private String email;
    private String avatar;
    private Integer diemTichLuy = 0;
    private String hangThanhVien = "Đồng";
    private Boolean trangThai = true;
}
