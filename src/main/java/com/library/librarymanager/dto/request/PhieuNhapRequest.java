package com.library.librarymanager.dto.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

@NotEmpty

public class PhieuNhapRequest {
    @Positive
    private int nhaCungCapId;
    @Positive
    private int nhanVienId;
    @Valid
    private List<ChiTietPhieuNhapRequest> list;
}
