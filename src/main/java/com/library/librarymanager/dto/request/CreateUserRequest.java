package com.library.librarymanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CreateUserRequest {
    @NotBlank(message = "Username không được trống")
    @Size(min = 3, message = "Username tối thiểu 3 ký tự")

    private String tenDangNhap;
    @NotBlank(message = "Password không được trống")
    @Size(min = 4, message = "Password tối thiểu 4 ký tự")

    private String matKhau;
    private String hoTen;
    private String sdt;
    private String tenNhom;
}




