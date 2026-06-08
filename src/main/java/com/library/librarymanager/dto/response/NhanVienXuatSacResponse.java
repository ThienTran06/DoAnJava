package com.library.librarymanager.dto.response;

import java.math.BigDecimal;

public interface NhanVienXuatSacResponse {
    Integer getNhanVienId();
    String getHoTen();
    String getUsername();
    Long getSoHoaDon();
    BigDecimal getDoanhThu();
}
