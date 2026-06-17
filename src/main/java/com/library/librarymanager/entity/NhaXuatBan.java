package com.library.librarymanager.entity;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "nha_xuat_ban")
public class NhaXuatBan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ten_nxb", nullable = false)
    @JsonProperty("tenNxb")
    @JsonAlias({"tenNXB", "tennxb"})
    private String tenNxb;

    @Column(name = "dia_chi")
    private String diaChi;
}
