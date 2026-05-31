package com.library.librarymanager.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.library.librarymanager.entity.KhachHang;
import com.library.librarymanager.repository.KhachHangRepository;
import com.library.librarymanager.service.Interface.CloudinaryService;
import com.library.librarymanager.service.Interface.KhachHangService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KhachHangServiceImpl implements KhachHangService {
    private final KhachHangRepository khachHangRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public List<KhachHang> getAll() {
        return khachHangRepository.findAll();
    }

    @Override
    public KhachHang getById(int id) {
        return khachHangRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy khách hàng có id = "+id));
    }

    private String tinhHangThanhVien(Integer diemTichLuy) {
        int diem = diemTichLuy == null ? 0 : diemTichLuy;
        if (diem >= 20000) return "Bạch Kim";
        if (diem >= 5000) return "Vàng";
        if (diem >= 1000) return "Bạc";
        return "Đồng";
    }

    @Override
    public KhachHang create(String hoTen, String sdt, String email, String ngaySinh, Integer diemTichLuy, MultipartFile avatar) {
        KhachHang khachHang = new KhachHang();
        khachHang.setHoTen(hoTen);
        khachHang.setSdt(sdt);
        khachHang.setEmail(email);
        khachHang.setNgaySinh(ngaySinh);
        int diem = diemTichLuy != null ? diemTichLuy : 0;
        khachHang.setDiemTichLuy(diem);
        khachHang.setHangThanhVien(tinhHangThanhVien(diem));
        khachHang.setTrangThai(true);
        if (avatar != null && !avatar.isEmpty()) {
            if (avatar.getContentType() == null || !avatar.getContentType().startsWith("image/"))
                throw new RuntimeException("File phải là ảnh");
            if (avatar.getSize() > 10 * 1024 * 1024)
            throw new RuntimeException("Ảnh vượt quá 10MB");
            khachHang.setAvatar(cloudinaryService.uploadFile(avatar));
        }
        return khachHangRepository.save(khachHang);
    }

    @Override
    public KhachHang updateById(int id, String hoTen, String sdt, String email, String ngaySinh, Integer diemTichLuy, Boolean trangThai, MultipartFile avatar) {
        KhachHang res = khachHangRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khách hàng có id = " + id));
        res.setHoTen(hoTen);
        res.setSdt(sdt);
        res.setEmail(email);
        if (ngaySinh != null) res.setNgaySinh(ngaySinh);
        if (diemTichLuy != null) res.setDiemTichLuy(diemTichLuy);
        if (trangThai != null) res.setTrangThai(trangThai);
        if (avatar != null && !avatar.isEmpty()) {
            if (avatar.getContentType() == null || !avatar.getContentType().startsWith("image/"))
                throw new RuntimeException("File phải là ảnh");
            if (avatar.getSize() > 5 * 1024 * 1024)
                throw new RuntimeException("Ảnh vượt quá 5MB");
            res.setAvatar(cloudinaryService.uploadFile(avatar));
        }
        res.setHangThanhVien(tinhHangThanhVien(res.getDiemTichLuy()));
        return khachHangRepository.save(res);
    }
    @Override
    public void deleteById(int id) {
        KhachHang kh = khachHangRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khách hàng có id = " + id));
        kh.setTrangThai(false);
        khachHangRepository.save(kh);
    }
}
