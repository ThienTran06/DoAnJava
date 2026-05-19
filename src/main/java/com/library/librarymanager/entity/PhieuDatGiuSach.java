package com.library.librarymanager.entity;



import com.library.librarymanager.enums.TrangThaiGiu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phieu_dat_giu_sach")
public class PhieuDatGiuSach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int khachHangId;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    private TrangThaiGiu trangThai;
    @OneToMany(mappedBy = "phieuGiu")
    private List<ChiTietPhieuGiu> danhSachSach;
}
