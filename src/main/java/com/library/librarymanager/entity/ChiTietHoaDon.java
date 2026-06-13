package com.library.librarymanager.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_hoa_don")
public class ChiTietHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int soLuong;

    private BigDecimal donGia;

    private String tenSach;

    private String hinhAnh;

    private BigDecimal thanhTien;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="hoa_don_id")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "sach_id")
    private Sach sach;

    public String getHinhAnh() {
        if (isUnsafeImageUrl(hinhAnh) && sach != null) {
            return sach.getHinhAnh();
        }
        return hinhAnh;
    }

    private boolean isUnsafeImageUrl(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        String lower = value.toLowerCase();
        return lower.contains("zalo") || lower.contains("zaloapp") || lower.contains("zalo.me");
    }
}
