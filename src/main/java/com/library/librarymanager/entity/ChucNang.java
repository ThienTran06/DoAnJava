package com.library.librarymanager.entity;

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
@Table(name = "chuc_nang")
public class ChucNang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maChucNang;

    private String tenChucNang;


    @OneToMany(mappedBy = "chucNang")
    private List<PhanQuyen> dsPhanQuyen;

}