package com.library.librarymanager.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

    @JsonProperty("sdt")
    @JsonAlias({"SDT", "phone"})
    private String SDT;

    private String email;

    private String avatar;

    @Column(name = "diem_tich_luy", columnDefinition = "int default 0")
    private int diemTichLuy = 0;

    @Column(name = "hang_thanh_vien", columnDefinition = "varchar(50) default 'Đồng'")
    private String hangThanhVien;

    @Column(name = "vip", columnDefinition = "boolean default false")
    @JsonAlias({"isVip", "is_Vip", "is_vip"})
    private boolean vip = false;

    @PrePersist
    @PreUpdate
    private void normalizeDefaults() {
        if (diemTichLuy < 0) {
            diemTichLuy = 0;
        }
        if (hangThanhVien == null || hangThanhVien.isBlank()) {
            hangThanhVien = "Đồng";
        }
    }
}
