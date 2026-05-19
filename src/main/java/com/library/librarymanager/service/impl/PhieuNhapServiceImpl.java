package com.library.librarymanager.service.impl;

import com.library.librarymanager.dto.request.ChiTietPhieuNhapRequest;
import com.library.librarymanager.dto.request.PhieuNhapRequest;
import com.library.librarymanager.entity.*;
import com.library.librarymanager.repository.*;
;
import com.library.librarymanager.service.Interface.PhieuNhapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class PhieuNhapServiceImpl implements PhieuNhapService {
    private final PhieuNhapRepository phieuNhapRepository;
    private final NhaCungCapRepository nhaCungCapRepository;
    private final NguoiDungRepository nhanVienRepository;
    private final SachRepository sachRepository;
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    @Override
    public List<PhieuNhap> getAll() {
        return phieuNhapRepository.findAll();
    }

    @Override
    public PhieuNhap getById(int id) {
        return phieuNhapRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy phiếu nhập có id = "+id));
    }

    @Override
    public PhieuNhap create(PhieuNhapRequest request)
    {
        PhieuNhap phieuNhap = new PhieuNhap();
        NhaCungCap nhaCungCap = nhaCungCapRepository.findById(request.getNhaCungCapId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy nhà cung cấp có id = "+request.getNhaCungCapId()));
        phieuNhap.setNhaCungCap(nhaCungCap);
        phieuNhap.setNgayNhap(LocalDateTime.now());
        NguoiDung nhanVien = nhanVienRepository.findById(request.getNhanVienId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy nhân viên có id = "+request.getNhanVienId()));
        phieuNhap.setNhanVien(nhanVien);
        phieuNhapRepository.save(phieuNhap);
        BigDecimal tongTienNhap = BigDecimal.ZERO;
        List<ChiTietPhieuNhap> list = new ArrayList<>();
        for(ChiTietPhieuNhapRequest chiTietPhieuNhapRequest : request.getList()){
            Sach sach = sachRepository.findById(chiTietPhieuNhapRequest.getSachId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Không tìm thấy sách có id = "+chiTietPhieuNhapRequest.getSachId()));
            ChiTietPhieuNhap chiTietPhieuNhap = new ChiTietPhieuNhap();
            chiTietPhieuNhap.setPhieuNhap(phieuNhap);
            chiTietPhieuNhap.setSach(sach);
            chiTietPhieuNhap.setSoLuong(chiTietPhieuNhapRequest.getSoLuongNhap());
            chiTietPhieuNhap.setGiaNhap(chiTietPhieuNhapRequest.getGiaNhap());
            list.add(chiTietPhieuNhap);
            sach.setSoLuongTon(sach.getSoLuongTon()+chiTietPhieuNhapRequest.getSoLuongNhap());
            tongTienNhap = tongTienNhap.add(chiTietPhieuNhapRequest.getGiaNhap().multiply(BigDecimal.valueOf(chiTietPhieuNhapRequest.getSoLuongNhap())));
        }
        chiTietPhieuNhapRepository.saveAll(list);
        phieuNhap.setTongTien(tongTienNhap);
        phieuNhap.setDanhSachChiTiet(list);
        phieuNhapRepository.save(phieuNhap);
        return phieuNhap;
    }

    @Override
    public PhieuNhap updateById(int id, PhieuNhap phieuNhap) {
        PhieuNhap res = phieuNhapRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy phiếu nhập có id = "+id));

        res.setNgayNhap(phieuNhap.getNgayNhap());
        res.setTongTien(phieuNhap.getTongTien());
        res.setNhanVien(phieuNhap.getNhanVien());
        res.setNhaCungCap(phieuNhap.getNhaCungCap());
        phieuNhapRepository.save(res);
        return res;
    }

    @Override
    public void deleteById(int id) {
        phieuNhapRepository.deleteById(id);
    }
}
