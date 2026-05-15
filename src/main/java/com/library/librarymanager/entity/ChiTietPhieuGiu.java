package com.library.librarymanager.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter


@Entity
@Table(name = "chi_tiet_phieu_giu")
public class ChiTietPhieuGiu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int soLuong;

    @ManyToOne
    @JoinColumn(name = "phieu_giu_id")
    @JsonIgnore
    private PhieuDatGiuSach phieuGiu;

    @ManyToOne
    @JoinColumn(name = "sach_id")
    private Sach sach;
}