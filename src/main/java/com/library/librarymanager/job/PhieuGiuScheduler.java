package com.library.librarymanager.job;

import com.library.librarymanager.entity.PhieuDatGiuSach;
import com.library.librarymanager.enums.TrangThaiGiu;
import com.library.librarymanager.repository.PhieuDatGiuSachRepository;
import com.library.librarymanager.service.Interface.PhieuGiuSachService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PhieuGiuScheduler {

    private final PhieuDatGiuSachRepository phieuRepo;
    private final PhieuGiuSachService phieuService;

    public PhieuGiuScheduler(
            PhieuDatGiuSachRepository phieuRepo,
            PhieuGiuSachService phieuService
    ) {
        this.phieuRepo = phieuRepo;
        this.phieuService = phieuService;
    }

    @Scheduled(fixedRate = 60000)
    public void autoExpire() {

        List<PhieuDatGiuSach> ds = phieuRepo
                .findByTrangThaiAndExpiredAtBefore(
                        TrangThaiGiu.PENDING,
                        LocalDateTime.now()
                );

        for (PhieuDatGiuSach p : ds) {
            phieuService.expire(p.getId());
        }
    }
}