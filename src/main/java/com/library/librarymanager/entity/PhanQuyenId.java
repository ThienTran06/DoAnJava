package com.library.librarymanager.entity;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PhanQuyenId implements Serializable {
    private int nguoiDung;
    private int chucNang;

    public PhanQuyenId() {}
    public PhanQuyenId(int nguoiDung, int chucNang) {
        this.nguoiDung = nguoiDung;
        this.chucNang = chucNang;
    }
}