package com.library.librarymanager.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HoaDonRequest {
     private int khachHangId;
     private int nhanVienId;
     List<ChiTietHoaDonRequest> danhSachChiTiet;
}
