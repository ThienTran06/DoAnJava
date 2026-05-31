package com.library.librarymanager.dto.response;

import java.math.BigDecimal;

public class ThongKeHoaDon {

    private long tongHoaDon;

    private long hoaDonTrongNgay;

    private BigDecimal doanhThuTrongNgay;

    private BigDecimal tongDoanhThu;

    public ThongKeHoaDon(long tongHoaDon, long hoaDonTrongNgay,
                      BigDecimal doanhThuTrongNgay, BigDecimal tongDoanhThu) {
        this.tongHoaDon = tongHoaDon;
        this.hoaDonTrongNgay = hoaDonTrongNgay;
        this.doanhThuTrongNgay = doanhThuTrongNgay;
        this.tongDoanhThu = tongDoanhThu;
    }

    public long getTongHoaDon() { return tongHoaDon; }
    public long getHoaDonTrongNgay() { return hoaDonTrongNgay; }
    public BigDecimal getDoanhThuTrongNgay() { return doanhThuTrongNgay; }
    public BigDecimal getTongDoanhThu() { return tongDoanhThu; }
}