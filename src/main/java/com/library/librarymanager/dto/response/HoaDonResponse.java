package com.library.librarymanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonResponse {
    private int id;
    private BigDecimal tongTien;
    private BigDecimal tienGiamGia;
    private String maGiamGia;
    private String trangThai;
    private String qrUrl;
}
