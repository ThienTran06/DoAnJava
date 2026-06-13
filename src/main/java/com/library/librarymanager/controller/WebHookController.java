package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.WebHookRequest;
import com.library.librarymanager.entity.HoaDon;
import com.library.librarymanager.repository.HoaDonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class WebHookController {

    private final HoaDonRepository hoaDonRepository;

    private static final Pattern HD_PATTERN = Pattern.compile("HD\\d+");

    @Value("${app.webhook.sepay.secret:}")
    private String sepayWebhookSecret;

    @PostMapping("/sepay")
    public void handle(
            @RequestHeader(value = "X-Webhook-Secret", required = false) String webhookSecret,
            @RequestBody WebHookRequest req
    ) {
        validateWebhookSecret(webhookSecret);

        if (req == null || req.getContent() == null || req.getTransferAmount() == null) return;

        String content = req.getContent();

        Matcher matcher = HD_PATTERN.matcher(content);
        if (!matcher.find()) return;

        String ma = matcher.group();

        int id;
        try {
            id = Integer.parseInt(ma.replace("HD", ""));
        } catch (Exception e) {
            return;
        }

        HoaDon hd = hoaDonRepository.findById(id).orElse(null);
        if (hd == null) return;

        if ("PAID".equals(hd.getTrangThai())) return;

        BigDecimal soTienNhan = req.getTransferAmount();
        BigDecimal tongTien = hd.getTongTien();

        if (tongTien == null) return;

        // CHECK SỐ TIỀN
        // Cho phép >= (tránh thiếu 1-2đ do làm tròn)
        if (soTienNhan.compareTo(tongTien) < 0) {
            return;
        }

        hd.setTrangThai("PAID");
        hd.setNgayBan(LocalDateTime.now());

        hoaDonRepository.save(hd);
    }

    private void validateWebhookSecret(String webhookSecret) {
        if (sepayWebhookSecret == null || sepayWebhookSecret.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Sepay webhook secret is not configured");
        }
        if (!Objects.equals(sepayWebhookSecret, webhookSecret)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid webhook secret");
        }
    }
}
