package com.library.librarymanager.entity;
import com.fasterxml.jackson.annotation.JsonAlias;
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
    private String SDT;
    private String email;
    private String avatar;
    private int diemTichLuy = 0;
    private String hangThanhVien = "Đồng";
    @JsonAlias({"isVip", "is_Vip", "is_vip"})
    private boolean vip = false;
    @PrePersist
    @PreUpdate
    private void normalizeDefaults() {
        if (hangThanhVien == null || hangThanhVien.isBlank()) {
            hangThanhVien = "Đồng";
        }
    }
}
