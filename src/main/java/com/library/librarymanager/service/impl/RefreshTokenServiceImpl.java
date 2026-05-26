package com.library.librarymanager.service.impl;


import com.library.librarymanager.entity.RefreshToken;
import com.library.librarymanager.repository.RefreshTokenRepository;
import com.library.librarymanager.service.Interface.RefreshTokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Autowired
    private RefreshTokenRepository repo;
    @Override
    @Transactional
    public RefreshToken create(String username) {

        String rawToken = generateSecureToken();

        RefreshToken rt = new RefreshToken();

        rt.setUsername(username);

        rt.setToken(hashToken(rawToken));

        rt.setRawToken(rawToken);

        rt.setExpiryDate(
                new Date(System.currentTimeMillis()
                        + 7L * 24 * 60 * 60 * 1000)
        );

        rt.setRevoked(false);

        return repo.save(rt);
    }
    @Override
    public RefreshToken validate(String token) {

        RefreshToken rt = repo.findByToken(hashToken(token))
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (rt.isRevoked() || rt.getExpiryDate().before(new Date())) {
            throw new RuntimeException("Refresh token expired or revoked");
        }

        return rt;
    }
    @Override
    @Transactional
    public void deleteByToken(String token) {
        repo.deleteByToken(hashToken(token));
    }
    @Override
    @Transactional
    public void revokeAllByUsername(String username) {
        List<RefreshToken> list = repo.findByUsername(username);

        for (RefreshToken rt : list) {
            rt.setRevoked(true);
        }

        repo.saveAll(list);
    }

    private String generateSecureToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot hash refresh token");
        }
    }
}
