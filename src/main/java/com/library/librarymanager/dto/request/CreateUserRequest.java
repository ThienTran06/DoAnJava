package com.library.librarymanager.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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
@Valid
public class CreateUserRequest {
    @NotBlank(message = "Username không được trống")
    @Size(min = 3, message = "Username tối thiểu 3 ký tự")
    private String tenDangNhap;
    
    @NotBlank(message = "Password không được trống")
    @Size(min = 4, message = "Password tối thiểu 4 ký tự")
    private String matKhau;


    @NotBlank(message = "hoTen không được trống")
    private String hoTen;

    @NotBlank(message = "Số điện thoại không được trống")
    @Size( min = 10, max=10, message = "Số điện thoại phải có 10 ký tư")
    @Pattern(regexp = "^[0-9]*$", message = "Số điện thoại chỉ được chứa các chữ số")
    private String sdt;

    @NotBlank(message = "tên nhóm không được trống")
    private String tenNhom;

    @NotEmpty(message = "List quyền không được trống")
    private List<Integer> permissionIds;
}




