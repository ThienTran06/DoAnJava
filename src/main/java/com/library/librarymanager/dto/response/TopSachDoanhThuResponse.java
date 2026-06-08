package com.library.librarymanager.dto.response;

import java.math.BigDecimal;

public interface TopSachDoanhThuResponse {
    Integer getSachId();
    String getTenSach();
    Long getSoLuongDaBan();
    BigDecimal getTongDoanhThu();
}
