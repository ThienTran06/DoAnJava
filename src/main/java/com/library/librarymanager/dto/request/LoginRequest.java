package com.library.librarymanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Username không được trống")
    @Size(min = 3, message = "Username tối thiểu 3 ký tự")
    private String username;

    @NotBlank(message = "Password không được trống")
    @Size(min = 4, message = "Password tối thiểu 4 ký tự")
    private String password;
}
