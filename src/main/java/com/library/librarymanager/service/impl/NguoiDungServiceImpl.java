package com.library.librarymanager.service.impl;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.librarymanager.Exception.AuthException;
import com.library.librarymanager.dto.request.CreateUserRequest;
import com.library.librarymanager.entity.ChucNang;
import com.library.librarymanager.entity.NguoiDung;
import com.library.librarymanager.entity.NhomNguoiDung;
import com.library.librarymanager.entity.PhanQuyen;
import com.library.librarymanager.repository.ChucNangRepository;
import com.library.librarymanager.repository.NguoiDungRepository;
import com.library.librarymanager.repository.NhomNguoiDungRepository;
import com.library.librarymanager.repository.PhanQuyenRepository;
import com.library.librarymanager.service.Interface.CloudinaryService;
import com.library.librarymanager.service.Interface.NguoiDungService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NguoiDungServiceImpl implements NguoiDungService {

    private final NguoiDungRepository repo;
    private final NhomNguoiDungRepository nhomRepo;
    private final PhanQuyenRepository phanQuyenRepo;
    private final ChucNangRepository chucNangRepo;
    private final BCryptPasswordEncoder encoder;
    private final CloudinaryService cloudinaryService;

    @Override
    public List<NguoiDung> getAll() {
        return repo.findAll();
    }

    @Override
    public NguoiDung getById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new AuthException("Người dùng không tồn tại"));
    }

    @Override
    @Transactional
    public void addAllPermissions(int id) {
        NguoiDung nd = repo.findById(id)
                .orElseThrow(() -> new AuthException("Người dùng không tồn tại"));
        List<ChucNang> dsChucNang = chucNangRepo.findAll();
        for (ChucNang cn : dsChucNang) {
            if (phanQuyenRepo.existsByNguoiDungAndChucNang(nd, cn)) continue;
            PhanQuyen pq = new PhanQuyen();
            pq.setNguoiDung(nd);
            pq.setChucNang(cn);
            phanQuyenRepo.save(pq);
        }
    }

    @Override
    @Transactional
    public void addStaffPermissions(NguoiDung nd) {
        List<ChucNang> ds = chucNangRepo.findByTenChucNangStartingWith("QUAN_LY");
        for (ChucNang cn : ds) {
            if (phanQuyenRepo.existsByNguoiDungAndChucNang(nd, cn)) continue;
            PhanQuyen pq = new PhanQuyen();
            pq.setNguoiDung(nd);
            pq.setChucNang(cn);
            phanQuyenRepo.save(pq);
        }
    }

    @Override
    @Transactional
    public NguoiDung create(CreateUserRequest req) {
        if (repo.findByUsername(req.getTenDangNhap()).isPresent()) {
            throw new RuntimeException("Tài khoản đã tồn tại");
        }
        NhomNguoiDung nhom = nhomRepo.findByTenNhom(req.getTenNhom())
                .orElseThrow(() -> new AuthException("Nhóm không tồn tại"));
        NguoiDung nd = new NguoiDung();
        nd.setChucVu(req.getChucVu());
        nd.setUsername(req.getTenDangNhap());
        nd.setPassword(encoder.encode(req.getMatKhau()));
        nd.setSDT(req.getSdt());
        nd.setHoTen(req.getHoTen());
        nd.setNhom(nhom);
        nd.setEmail(req.getEmail());
        nd.setCaLamViec(req.getCaLamViec());
        nd.setDiaChi(req.getDiaChi());
        nd.setNgayVaoLam(req.getNgayVaoLam());
        nd.setLuongCoBan(req.getLuongCoBan());
        nd.setGhiChu(req.getGhiChu());
        if (req.getAvatarFile() != null && !req.getAvatarFile().isEmpty()) {
            nd.setAvatar(cloudinaryService.uploadFile(req.getAvatarFile()));
        }
        NguoiDung saved = repo.save(nd);
        if (nhom.getTenNhom().equals("ADMIN")) {
            addAllPermissions(saved.getId());
        } else if (req.getPermissionIds() != null && !req.getPermissionIds().isEmpty()) {
            updatePermissions(saved.getId(), req.getPermissionIds());
        }
        return saved;
    }

    @Transactional
    @Override
    public NguoiDung update(int id, CreateUserRequest req) {
        NguoiDung nd = repo.findById(id)
                .orElseThrow(() -> new AuthException("Người dùng không tồn tại"));
        if (!nd.getUsername().equals(req.getTenDangNhap())
                && repo.findByUsername(req.getTenDangNhap()).isPresent()) {
            throw new RuntimeException("Tài khoản đã tồn tại");
        }
        NhomNguoiDung nhom = nhomRepo.findByTenNhom(req.getTenNhom())
                .orElseThrow(() -> new AuthException("Nhóm không tồn tại"));
        nd.setUsername(req.getTenDangNhap());
        if (req.getMatKhau() != null && !req.getMatKhau().isBlank()) {
            nd.setPassword(encoder.encode(req.getMatKhau()));
        }
        nd.setChucVu(req.getChucVu());
        nd.setHoTen(req.getHoTen());
        nd.setSDT(req.getSdt());
        nd.setNhom(nhom);
        nd.setEmail(req.getEmail());
        nd.setCaLamViec(req.getCaLamViec());
        nd.setDiaChi(req.getDiaChi());
        nd.setNgayVaoLam(req.getNgayVaoLam());
        nd.setLuongCoBan(req.getLuongCoBan());
        nd.setGhiChu(req.getGhiChu());
        if (req.getAvatarFile() != null && !req.getAvatarFile().isEmpty()) {
            nd.setAvatar(cloudinaryService.uploadFile(req.getAvatarFile()));
        }
        return repo.save(nd);



    }

    @Transactional
    @Override
    public void delete(int id) {
        NguoiDung nd = repo.findById(id)
                .orElseThrow(() -> new AuthException("Người dùng không tồn tại"));
        phanQuyenRepo.deleteAllByNguoiDung(nd);
        repo.delete(nd);
    }

    @Transactional
    @Override
    public void updatePermissions(int id, List<Integer> permissionIds) {
        NguoiDung nd = repo.findById(id)
                .orElseThrow(() -> new AuthException("Người dùng không tồn tại"));
        if (permissionIds == null) {
            throw new AuthException("Danh sách quyền không được null");
        }
        List<Integer> uniqueIds = permissionIds.stream().distinct().toList();
        List<ChucNang> permissions = chucNangRepo.findAllById(uniqueIds);
        if (permissions.size() != uniqueIds.size()) {
            List<Integer> foundIds = permissions.stream().map(ChucNang::getMaChucNang).toList();
            List<Integer> missingIds = uniqueIds.stream().filter(i -> !foundIds.contains(i)).toList();
            throw new AuthException("Quyền không tồn tại: " + missingIds);
        }
        phanQuyenRepo.deleteAllByNguoiDung(nd);
        for (ChucNang cn : permissions) {
            PhanQuyen pq = new PhanQuyen();
            pq.setNguoiDung(nd);
            pq.setChucNang(cn);
            phanQuyenRepo.save(pq);
        }
    }
}