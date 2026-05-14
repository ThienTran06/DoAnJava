package com.library.librarymanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "nhom_nguoi_dung")
public class NhomNguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tenNhom;
    @OneToMany(mappedBy = "nhom")
    @JsonIgnore
    private List<NguoiDung> dsNguoiDung;

}