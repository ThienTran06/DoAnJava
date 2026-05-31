package com.library.librarymanager.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String ngaySinh;
}
