package com.library.librarymanager.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendOtpRequest {

    @NotBlank(message = "Username khong duoc trong")
    private String username;

    @NotBlank(message = "Email khong duoc trong")
    @Email(message = "Email khong hop le")
    private String email;
}
