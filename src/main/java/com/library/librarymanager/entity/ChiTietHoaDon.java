package com.library.librarymanager.entity;
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
    @ManyToOne
    @JoinColumn(name="hoa_don_id")
    private HoaDon hoaDon;
    @ManyToOne
    @JoinColumn(name = "sach_id")
    private Sach sach;
}
