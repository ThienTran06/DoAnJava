package com.library.librarymanager.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UuDaiRequest {
    @NotBlank(message = "Ten uu dai khong duoc trong")
    private String tenUuDai;

    @NotNull(message = "Phan tram giam khong duoc trong")
    @DecimalMin(value = "0.01", message = "Phan tram giam phai lon hon 0")
    @DecimalMax(value = "100.00", message = "Phan tram giam khong duoc vuot qua 100")
    private BigDecimal phanTramGiam;

    @NotNull(message = "Ngay bat dau khong duoc trong")
    private LocalDate ngayBatDau;

    @NotNull(message = "Ngay ket thuc khong duoc trong")
    private LocalDate ngayKetThuc;

    private Boolean trangThai = true;

    @NotEmpty(message = "Vui long chon it nhat mot sach ap dung uu dai")
    private List<Integer> sachIds;
}
