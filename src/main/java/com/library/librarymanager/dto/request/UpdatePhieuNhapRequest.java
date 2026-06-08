package com.library.librarymanager.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdatePhieuNhapRequest {
    private LocalDateTime ngayNhap;

    @Positive(message = "Nha cung cap id phai lon hon 0")
    private Integer nhaCungCapId;

    @Positive(message = "Nhan vien id phai lon hon 0")
    private Integer nhanVienId;
}
