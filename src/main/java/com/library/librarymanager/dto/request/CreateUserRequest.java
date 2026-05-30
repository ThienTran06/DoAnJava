package com.library.librarymanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "Username khong duoc trong")
    @Size(min = 3, message = "Username toi thieu 3 ky tu")
    private String tenDangNhap;

    @NotBlank(message = "Password khong duoc trong")
    @Size(min = 4, message = "Password toi thieu 4 ky tu")
    private String matKhau;

    @NotBlank(message = "Ho ten khong duoc trong")
    private String hoTen;

    @NotBlank(message = "So dien thoai khong duoc trong")
    @Size(min = 10, max = 10, message = "So dien thoai phai co 10 ky tu")
    @Pattern(regexp = "^[0-9]*$", message = "So dien thoai chi duoc chua cac chu so")
    private String sdt;

    @NotBlank(message = "Ten nhom khong duoc trong")
    private String tenNhom;

    private List<@Positive(message = "Permission id phai lon hon 0") Integer> permissionIds;
}
