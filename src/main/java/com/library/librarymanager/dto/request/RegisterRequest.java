package com.library.librarymanager.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String hoTen;
    private String username;
    private String password;
    private String sdt;
    private String vaiTro;
}
