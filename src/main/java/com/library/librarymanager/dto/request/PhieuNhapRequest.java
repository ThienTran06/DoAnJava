package com.library.librarymanager.dto.request;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PhieuNhapRequest {
    private int nhaCungCapId;
    private int nhanVienId;
    private List<ChiTietPhieuNhapRequest> list;
}
