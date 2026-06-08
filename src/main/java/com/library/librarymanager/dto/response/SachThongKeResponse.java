package com.library.librarymanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SachThongKeResponse {
    private long tongDauSach;
    private int tongSoLuongTon;
    private long soSachHetHang;
    private BigDecimal giaTriKho;
    private String tenSachBanChay;
    private int soLuongDaBanNhieuNhat;
}
