package com.library.librarymanager.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateHoaDonRequest {
    private String maHoaDon;
    private LocalDateTime ngayBan;

    @Positive(message = "Khach hang id phai lon hon 0")
    private Integer khachHangId;

    @Positive(message = "Nhan vien id phai lon hon 0")
    private Integer nhanVienId;
}
