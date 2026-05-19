package com.library.librarymanager.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ChiTietPhieuNhapRequest {
    private int sachId;
    private int soLuongNhap;
    private BigDecimal giaNhap;
}
