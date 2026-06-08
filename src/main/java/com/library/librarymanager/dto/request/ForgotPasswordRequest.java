package com.library.librarymanager.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {

    @NotBlank(message = "Username khong duoc trong")
    private String username;

    @NotBlank(message = "Email khong duoc trong")
    @Email(message = "Email khong hop le")
    private String email;

    @NotBlank(message = "Password moi khong duoc trong")
    @Size(min = 4, message = "Password toi thieu 4 ky tu")
    private String newPassword;
}
