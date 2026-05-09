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
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String maHoaDon;
    private LocalDateTime ngayBan;
    private BigDecimal tongTien;
    private String trangThai;
    @ManyToOne
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;
    @ManyToOne
    @JoinColumn(name="nhan_vien_id")
    private NhanVien nhanVien;
    @OneToMany
    private List<ChiTietHoaDon> danhSachChiTiet;
}
