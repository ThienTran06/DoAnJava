package com.library.librarymanager.entity;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode
public class PhanQuyenId implements Serializable {

    private int nguoiDung;
    private int chucNang;

    public PhanQuyenId() {}
}