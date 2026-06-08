package com.library.librarymanager.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class KpiNgayRequest {
    @NotNull(message = "Muc tieu doanh thu khong duoc null")
    @Positive(message = "Muc tieu doanh thu phai lon hon 0")
    private BigDecimal mucTieuDoanhThu;
}
