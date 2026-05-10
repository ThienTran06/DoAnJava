package com.library.librarymanager.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_phieu_nhap")
public class ChiTietPhieuNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int soLuong;
    private BigDecimal giaNhap;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="phieu_nhap_id")
    private PhieuNhap phieuNhap;
    @ManyToOne
    @JoinColumn(name="sach_id")
    private Sach sach;
}
