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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class WebHookController {

    private final HoaDonRepository hoaDonRepository;

    private static final Pattern HD_PATTERN = Pattern.compile("HD\\d+");

    @Value("${app.webhook.sepay.api-key}")
    private String sepayApiKey;

    @PostMapping("/sepay")
    public void handle(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody WebHookRequest req
    ) {

        validateApiKey(authorization);

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

        if (soTienNhan.compareTo(tongTien) < 0) return;

        hd.setTrangThai("PAID");
        hd.setNgayBan(LocalDateTime.now());

        hoaDonRepository.save(hd);
    }

    private void validateApiKey(String authorization) {
        if (sepayApiKey == null || sepayApiKey.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "SePay API key is not configured"
            );
        }

        if (authorization == null || !authorization.equals("Apikey " + sepayApiKey)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid API key"
            );
        }
    }
}