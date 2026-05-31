package com.library.librarymanager.dto.response;

public class ThongKePhieuNhapResponse {
    private long phieuNhapTrongNgay;
    private long tongPhieuNhap;
    private double giaTriNhapTrongNgay;
    private double tongGiaTriNhap;

    public ThongKePhieuNhapResponse() {}

    public ThongKePhieuNhapResponse(long phieuNhapTrongNgay, long tongPhieuNhap,
                               double giaTriNhapTrongNgay, double tongGiaTriNhap) {
        this.phieuNhapTrongNgay = phieuNhapTrongNgay;
        this.tongPhieuNhap = tongPhieuNhap;
        this.giaTriNhapTrongNgay = giaTriNhapTrongNgay;
        this.tongGiaTriNhap = tongGiaTriNhap;
    }

    public long getPhieuNhapTrongNgay() {
        return phieuNhapTrongNgay;
    }

    public long getTongPhieuNhap() {
        return tongPhieuNhap;
    }

    public double getGiaTriNhapTrongNgay() {
        return giaTriNhapTrongNgay;
    }

    public double getTongGiaTriNhap() {
        return tongGiaTriNhap;
    }
}