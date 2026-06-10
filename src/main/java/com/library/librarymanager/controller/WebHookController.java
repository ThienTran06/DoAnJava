package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.WebHookRequest;
import com.library.librarymanager.entity.HoaDon;
import com.library.librarymanager.repository.HoaDonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/sepay")
    public void handle(@RequestBody WebHookRequest req) {

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
}