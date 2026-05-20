package com.library.librarymanager.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Positive
@NotEmpty
@Valid
public class HoaDonRequest {
     private int khachHangId;
     private int nhanVienId;
     List<ChiTietHoaDonRequest> danhSachChiTiet;
}
