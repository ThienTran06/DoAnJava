package com.library.librarymanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ma_giam_gia")
public class MaGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 40)
    private String ma;

    @Column(nullable = false)
    private BigDecimal giaTriGiam;

    @Column(nullable = false)
    private int diemDaDoi;

    @Column(nullable = false, length = 30)
    private String trangThai;

    private LocalDateTime ngayTao;

    private LocalDateTime ngayHetHan;

    private LocalDateTime ngaySuDung;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "khach_hang_id", nullable = false)
    private KhachHang khachHang;

    @PrePersist
    private void prePersist() {
        if (trangThai == null || trangThai.isBlank()) {
            trangThai = "CHUA_SU_DUNG";
        }
        if (ngayTao == null) {
            ngayTao = LocalDateTime.now();
        }
    }
}
