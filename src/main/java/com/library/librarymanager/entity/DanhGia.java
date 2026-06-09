package com.library.librarymanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "danh_gia")
@Getter
@Setter
@NoArgsConstructor
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "sach_id", nullable = true)
    private Sach sach;

    private int diemSao;

    @Column(columnDefinition = "TEXT")
    private String noiDung;

    private String loai; // "SACH" hoặc "DICH_VU"

    private String trangThai; // "CHO_DUYET", "DA_DUYET", "DA_XOA"

    @Column(columnDefinition = "TEXT")
    private String reply; // Reply từ cửa hàng

    @Column(name = "reply_at")
    private LocalDateTime replyAt; // Thời gian reply

    @Column(name = "reply_by")
    private String replyBy; // Người reply (tên nhân viên)

    @Column(columnDefinition = "LONGTEXT")
    private String hinhAnh; // URL ảnh hoặc JSON array ảnh

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
