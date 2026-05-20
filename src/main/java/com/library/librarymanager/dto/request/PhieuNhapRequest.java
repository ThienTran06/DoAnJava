package com.library.librarymanager.dto.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Positive
@NotEmpty
@Valid
public class PhieuNhapRequest {
    private int nhaCungCapId;
    private int nhanVienId;
    private List<ChiTietPhieuNhapRequest> list;
}
