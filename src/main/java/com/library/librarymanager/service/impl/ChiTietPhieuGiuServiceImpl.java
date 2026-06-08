package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.*;
import com.library.librarymanager.enums.TrangThaiGiu;
import com.library.librarymanager.repository.*;
import com.library.librarymanager.service.Interface.ChiTietPhieuGiuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChiTietPhieuGiuServiceImpl implements ChiTietPhieuGiuService {

    private final ChiTietPhieuGiuRepository ctRepo;
    private final PhieuDatGiuSachRepository phieuRepo;
    private final SachRepository sachRepo;

    private final int maxSach = 5;

    public ChiTietPhieuGiuServiceImpl(
            ChiTietPhieuGiuRepository ctRepo,
            PhieuDatGiuSachRepository phieuRepo,
            SachRepository sachRepo
    ) {
        this.ctRepo = ctRepo;
        this.phieuRepo = phieuRepo;
        this.sachRepo = sachRepo;
    }

    @Override
    @Transactional
    public void themSach(int phieuId, int sachId, int soLuong) {

        PhieuDatGiuSach p = phieuRepo.findById(phieuId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu"));

        if (p.getTrangThai() != TrangThaiGiu.PENDING) {
            throw new RuntimeException("Phiếu không hợp lệ");
        }
        if(soLuong<=0)throw new IllegalArgumentException("Số lượng không được âm");
        List<ChiTietPhieuGiu> ds = ctRepo.findByPhieuGiuId(phieuId);
        boolean daCoTrongPhieu = ds.stream()
                .anyMatch(ct -> ct.getSach() != null && ct.getSach().getId() == sachId);
        if (daCoTrongPhieu) {
            throw new RuntimeException("Khong duoc them trung mot sach trong phieu giu");
        }

        if (ds.size() >= maxSach) {
            throw new RuntimeException("Vuơt giới hạn sách");
        }

        int tong = ds.stream().mapToInt(ChiTietPhieuGiu::getSoLuong).sum();
        if (tong + soLuong > maxSach) {
            throw new RuntimeException("Vuợt giới hạn số lượng sách");
        }

        boolean daGiu = ctRepo.existsBySach_IdAndPhieuGiu_IdNotAndPhieuGiu_TrangThai(
                sachId, phieuId, TrangThaiGiu.PENDING
        );

        if (daGiu) {
            throw new RuntimeException("Sách đã được giữ");
        }

        Sach s = sachRepo.findByIdForUpdate(sachId) .orElseThrow(() -> new RuntimeException("Không tìm thấy sách"));;



        if (s.getSoLuongTon() < soLuong) {
            throw new RuntimeException("Không đủ số lượng");
        }

        s.setSoLuongTon(s.getSoLuongTon() - soLuong);
        sachRepo.save(s);

        ChiTietPhieuGiu ct = new ChiTietPhieuGiu();
        ct.setPhieuGiu(p);
        ct.setSach(s);
        ct.setSoLuong(soLuong);

        ctRepo.save(ct);
    }
    @Override
    public List<ChiTietPhieuGiu> getAll() {
        return ctRepo.findAll();
    }
    @Override
    @Transactional
    public void xoaSach(int chiTietId) {

        ChiTietPhieuGiu ct = ctRepo.findById(chiTietId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách"));

        Sach s = sachRepo.findByIdForUpdate(ct.getSach().getId()) .orElseThrow(() -> new RuntimeException("Khong tim thay sach"));;

        s.setSoLuongTon(s.getSoLuongTon() + ct.getSoLuong());
        sachRepo.save(s);

        ctRepo.delete(ct);
    }
}
