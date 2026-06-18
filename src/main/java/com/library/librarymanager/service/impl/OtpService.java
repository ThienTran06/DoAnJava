package com.library.librarymanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpService {

    private static final int OTP_LENGTH = 6;
    private static final long OTP_EXPIRY_SECONDS = 300; // 5 minutes

    private final JavaMailSender mailSender;

    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();

    private record OtpEntry(String otp, String email, Instant expiresAt) {}

    public void generateAndSend(String username, String email) {
        String otp = generateOtp();
        Instant expiresAt = Instant.now().plusSeconds(OTP_EXPIRY_SECONDS);
        otpStore.put(username.toLowerCase(), new OtpEntry(otp, email.toLowerCase(), expiresAt));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("BookHouse - Mã xác nhận OTP");
        message.setText(
                "Xin chào,\n\n"
                        + "Mã OTP của bạn là: " + otp + "\n"
                        + "Mã này sẽ hết hạn sau 5 phút.\n\n"
                        + "Nếu bạn không yêu cầu đổi mật khẩu, vui lòng bỏ qua email này.\n\n"
                        + "Trân trọng,\nBookHouse"
        );

        mailSender.send(message);
    }

    public boolean verify(String username, String otp) {
        OtpEntry entry = otpStore.get(username.toLowerCase());
        if (entry == null) return false;
        if (Instant.now().isAfter(entry.expiresAt())) {
            otpStore.remove(username.toLowerCase());
            return false;
        }
        if (!entry.otp().equals(otp)) return false;
        otpStore.remove(username.toLowerCase());
        return true;
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
