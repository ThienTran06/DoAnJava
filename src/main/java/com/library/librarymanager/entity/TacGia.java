package com.library.librarymanager.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tac_gia")
public class TacGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @Column(name = "quoc_tich")
    private String quocTich;
}
