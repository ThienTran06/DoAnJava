package com.library.librarymanager.service.impl;


import com.library.librarymanager.entity.RefreshToken;
import com.library.librarymanager.repository.RefreshTokenRepository;
import com.library.librarymanager.service.Interface.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private RefreshTokenRepository repo;
    @Override
    public RefreshToken create(String username) {

        RefreshToken rt = new RefreshToken();

        rt.setUsername(username);

        rt.setToken(UUID.randomUUID().toString());

        rt.setExpiryDate(
                new Date(System.currentTimeMillis()
                        + 7L * 24 * 60 * 60 * 1000)
        );

        rt.setRevoked(false);

        return repo.save(rt);
    }
    @Override
    public RefreshToken validate(String token) {

        RefreshToken rt = repo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (rt.isRevoked() || rt.getExpiryDate().before(new Date())) {
            throw new RuntimeException("Refresh token expired or revoked");
        }

        return rt;
    }
    @Override
    public void deleteByToken(String token) {
        repo.deleteByToken(token);
    }
    @Override
    public void revokeAllByUsername(String username) {
        List<RefreshToken> list = repo.findByUsername(username);

        for (RefreshToken rt : list) {
            rt.setRevoked(true);
        }

        repo.saveAll(list);
    }
}