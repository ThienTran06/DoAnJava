package com.library.librarymanager.config;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final String SECRET =
            "mysecretkeymysecretkeymysecretkey";

    public String generateToken(String username,
                                String role,
                                List<String> permissions) {

        try {

            JWSSigner signer =
                    new MACSigner(SECRET.getBytes());

            JWTClaimsSet claims = new JWTClaimsSet.Builder()

                    .subject(username)

                    .claim("role", role)

                    .claim("permissions", permissions)

                    .issueTime(new Date())

                    .expirationTime(
                            new Date(System.currentTimeMillis() + 300000)
                    )

                    .build();

            SignedJWT jwt = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claims
            );

            jwt.sign(signer);

            return jwt.serialize();

        }
        catch (Exception e) {
            throw new RuntimeException("Lỗi tạo token");
        }
    }

    public String extractUsername(String token) {

        try {

            SignedJWT jwt = SignedJWT.parse(token);

            JWSVerifier verifier =
                    new MACVerifier(SECRET.getBytes());

            if (!jwt.verify(verifier)) {
                throw new RuntimeException("Token không hợp lệ");
            }

            Date exp =
                    jwt.getJWTClaimsSet().getExpirationTime();

            if (exp.before(new Date())) {
                throw new RuntimeException("Token hết hạn");
            }

            return jwt.getJWTClaimsSet().getSubject();

        }
        catch (Exception e) {
            throw new RuntimeException("Token lỗi");
        }
    }

    public String extractRole(String token) {

        try {

            SignedJWT jwt = SignedJWT.parse(token);

            JWSVerifier verifier =
                    new MACVerifier(SECRET.getBytes());

            if (!jwt.verify(verifier)) {
                throw new RuntimeException("Token không hợp lệ");
            }

            return jwt.getJWTClaimsSet()
                    .getStringClaim("role");

        }
        catch (Exception e) {
            throw new RuntimeException("Token lỗi");
        }
    }

    public List<String> extractPermissions(String token) {

        try {

            SignedJWT jwt = SignedJWT.parse(token);

            JWSVerifier verifier =
                    new MACVerifier(SECRET.getBytes());

            if (!jwt.verify(verifier)) {
                throw new RuntimeException("Token không hợp lệ");
            }

            return jwt.getJWTClaimsSet().getStringListClaim("permissions");

        }
        catch (Exception e) {
            throw new RuntimeException("Token lỗi");
        }
    }

    public boolean validateToken(String token) {

        try {

            SignedJWT jwt = SignedJWT.parse(token);

            JWSVerifier verifier =
                    new MACVerifier(SECRET.getBytes());

            if (!jwt.verify(verifier)) {
                return false;
            }

            Date exp =
                    jwt.getJWTClaimsSet().getExpirationTime();

            return !exp.before(new Date());

        }
        catch (Exception e) {
            return false;
        }
    }
}