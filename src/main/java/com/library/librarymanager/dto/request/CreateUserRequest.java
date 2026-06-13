package com.library.librarymanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "Username khong duoc trong")
    @Size(min = 3, message = "Username toi thieu 3 ky tu")
    private String tenDangNhap;

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

    @Email(message = "Email khong dung dinh dang")
    private String email;

    private String avatar;

    private String caLamViec;

    private String diaChi;

    @PositiveOrZero(message = "Luong co ban khong duoc am")
    private BigDecimal luongCoBan;

    private String ghiChu;
}
