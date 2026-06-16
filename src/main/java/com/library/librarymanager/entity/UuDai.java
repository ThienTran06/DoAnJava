package com.library.librarymanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "uu_dai")
public class UuDai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String tenUuDai;

    @Column(nullable = false)
    private BigDecimal phanTramGiam;

    @Column(nullable = false)
    private LocalDate ngayBatDau;

    @Column(nullable = false)
    private LocalDate ngayKetThuc;

    @Column(nullable = false)
    private Boolean trangThai = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "uu_dai_sach",
            joinColumns = @JoinColumn(name = "uu_dai_id"),
            inverseJoinColumns = @JoinColumn(name = "sach_id")
    )
    private List<Sach> danhSachSach;
}
