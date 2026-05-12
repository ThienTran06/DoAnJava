package com.library.librarymanager.service.Interface;

import com.library.librarymanager.dto.request.LoginRequest;
import com.library.librarymanager.dto.response.LoginResponse;

public interface AuthService {    LoginResponse login(LoginRequest req);

}
