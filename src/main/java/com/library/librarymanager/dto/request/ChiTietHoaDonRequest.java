package com.library.librarymanager.dto.request;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChiTietHoaDonRequest {
    @Positive(message = "Sach id phai lon hon 0")
    private int sachID;

    @Positive(message = "So luong phai lon hon 0")
    private int soLuong;
}
