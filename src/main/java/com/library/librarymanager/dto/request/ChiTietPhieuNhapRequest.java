package com.library.librarymanager.dto.request;

import java.math.BigDecimal;

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
public class ChiTietPhieuNhapRequest {
    private int sachId;
    private int soLuongNhap;
    private BigDecimal giaNhap;
}
