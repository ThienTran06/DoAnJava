package com.library.librarymanager.dto.request;
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
public class ChiTietHoaDonRequest {
    private int sachID;
    private int soLuong;
}
