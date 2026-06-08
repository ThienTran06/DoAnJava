package com.library.librarymanager.config;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final byte[] secret;

    // Giữ cách lấy cấu hình bảo mật từ application.properties của Bản 1
    public JwtUtil(
            @Value("${jwt.secret}") String secret
    ) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("jwt.secret must be at least 32 characters");
        }
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
    }

    // Tận dụng hàm parseToken dùng chung cực sạch của Bản 2
    private SignedJWT parseToken(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret);

            if (!jwt.verify(verifier)) {
                throw new RuntimeException("Token không hợp lệ");
            }

            Date exp = jwt.getJWTClaimsSet().getExpirationTime();
            if (exp.before(new Date())) {
                throw new RuntimeException("Token hết hạn");
            }

            return jwt;
        } catch (Exception e) {
            throw new RuntimeException("Token lỗi");
        }
    }

    // Giữ hàm generateToken có thêm ID của Bản 2
    public String generateToken(Integer id, String username, String role, List<String> permissions) {
        try {
            JWSSigner signer = new MACSigner(secret);

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(username)
                    .claim("id", id) // Thêm ID người dùng
                    .claim("role", role)
                    .claim("permissions", permissions)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + 18000000)) // 5 tiếng
                    .build();

            SignedJWT jwt = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claims
            );

            jwt.sign(signer);
            return jwt.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo token");
        }
    }

    public Integer extractId(String token) {
        try {
            SignedJWT jwt = parseToken(token);
            return jwt.getJWTClaimsSet().getIntegerClaim("id");
        } catch (Exception e) {
            throw new RuntimeException("Token lỗi");
        }
    }

    public String extractUsername(String token) {
        try {
            SignedJWT jwt = parseToken(token);
            return jwt.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token lỗi");
        }
    }

    public String extractRole(String token) {
        try {
            SignedJWT jwt = parseToken(token);
            return jwt.getJWTClaimsSet().getStringClaim("role");
        } catch (Exception e) {
            throw new RuntimeException("Token lỗi");
        }
    }

    public List<String> extractPermissions(String token) {
        try {
            SignedJWT jwt = parseToken(token);
            return jwt.getJWTClaimsSet().getStringListClaim("permissions");
        } catch (Exception e) {
            throw new RuntimeException("Token lỗi");
        }
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}