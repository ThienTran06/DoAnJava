package com.library.librarymanager.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KhachHangLoginRequest {
    private String email;
    private String password;
}
