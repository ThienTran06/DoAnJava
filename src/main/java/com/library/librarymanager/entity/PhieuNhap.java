package com.library.librarymanager.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "phieu_nhap")
public class PhieuNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime ngayNhap;
    private BigDecimal tongTien;
    @ManyToOne
    @JoinColumn(name="nha_cung_cap_id")
    private NhaCungCap nhaCungCap;
    @ManyToOne
    @JoinColumn(name = "nhan_vien_id")
    private NguoiDung nhanVien;
    @OneToMany
    private List<ChiTietPhieuNhap> danhSachChiTiet;
}
