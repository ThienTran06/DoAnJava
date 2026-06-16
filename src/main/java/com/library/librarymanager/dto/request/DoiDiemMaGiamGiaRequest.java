package com.library.librarymanager.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoiDiemMaGiamGiaRequest {
    @Min(value = 100, message = "Can doi toi thieu 100 diem")
    private int diemSuDung;
}
