package com.library.librarymanager.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HoaDonRequest {
     @Positive
     private int khachHangId;

     @Positive
     private int nhanVienId;

     private String maGiamGia;

     @NotEmpty(message = "Danh sach chi tiet khong duoc trong")
     @Valid
     private List<ChiTietHoaDonRequest> danhSachChiTiet;
}
