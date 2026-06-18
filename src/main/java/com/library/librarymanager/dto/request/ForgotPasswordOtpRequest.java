package com.library.librarymanager.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordOtpRequest {

    @NotBlank(message = "Email khong duoc trong")
    @Email(message = "Email khong hop le")
    private String email;

    private String otp;
}
