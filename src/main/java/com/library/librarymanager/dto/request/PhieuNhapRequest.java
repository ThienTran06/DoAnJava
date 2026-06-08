package com.library.librarymanager.dto.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PhieuNhapRequest {
    @Positive
    private int nhaCungCapId;

    @Positive
    private int nhanVienId;

    @NotEmpty(message = "Danh sach chi tiet phieu nhap khong duoc trong")
    @Valid
    private List<ChiTietPhieuNhapRequest> list;
}
