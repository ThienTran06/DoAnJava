package com.library.librarymanager.service.Interface;

import com.library.librarymanager.entity.RefreshToken;

public interface RefreshTokenService {
    public RefreshToken create(String username);
    public RefreshToken validate(String token);
    public void deleteByToken(String token);
    public void revokeAllByUsername(String username);
}
