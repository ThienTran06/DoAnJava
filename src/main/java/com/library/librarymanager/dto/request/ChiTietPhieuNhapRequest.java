package com.library.librarymanager.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChiTietPhieuNhapRequest {
    @Positive(message = "Sach id phai lon hon 0")
    private int sachId;

    @Positive(message = "So luong nhap phai lon hon 0")
    private int soLuongNhap;

    @NotNull(message = "Gia nhap khong duoc null")
    @Positive(message = "Gia nhap phai lon hon 0")
    private BigDecimal giaNhap;
}
