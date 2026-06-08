package com.library.librarymanager.entity;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "nha_cung_cap")
public class NhaCungCap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ten_ncc")
    private String tenNCC;

    @JsonProperty("sdt")
    @JsonAlias({"SDT", "phone"})
    private String SDT;
    private String diaChi;
}
