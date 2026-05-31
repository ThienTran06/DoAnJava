package com.library.librarymanager.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "sach")
public class Sach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tenSach;
    private BigDecimal giaBan;
    private int soLuongTon;
    private String hinhAnh;
    private int namXuatBan;

    @ManyToOne
    @JoinColumn(name = "the_loai_id")
    private TheLoai theLoai;

    @ManyToOne
    @JoinColumn(name = "tac_gia_id")
    private TacGia tacGia;

    @ManyToOne
    @JoinColumn(name = "nha_xuat_ban_id")
    private NhaXuatBan nhaXuatBan;

}
