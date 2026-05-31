package com.library.librarymanager.service.impl;

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
import com.library.librarymanager.service.Interface.NguoiDungService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NguoiDungServiceImpl implements NguoiDungService {

    private final NguoiDungRepository repo;
    private final NhomNguoiDungRepository nhomRepo;
    private final PhanQuyenRepository phanQuyenRepo;
    private final ChucNangRepository chucNangRepo;
    private final BCryptPasswordEncoder encoder;
    private static final String ROLE_ADMIN = "ADMIN";
    private static final Map<String, List<String>> ROLE_PERMISSION_NAMES = Map.of(
            "THU_NGAN", List.of("QUAN_LY_HOA_DON", "QUAN_LY_KHACH_HANG", "QUAN_LY_PHIEU_GIU"),
            "NHAN_VIEN_BAN_HANG", List.of("QUAN_LY_SACH", "QUAN_LY_HOA_DON", "QUAN_LY_KHACH_HANG", "QUAN_LY_PHIEU_GIU"),
            "KHO", List.of("QUAN_LY_SACH", "QUAN_LY_PHIEU_NHAP", "QUAN_LY_NHA_CUNG_CAP"),
            "BIEN_MUC", List.of("QUAN_LY_SACH", "QUAN_LY_THE_LOAI", "QUAN_LY_TAC_GIA", "QUAN_LY_NHA_XUAT_BAN"),
            "KE_TOAN", List.of("XEM_BAO_CAO", "QUAN_LY_HOA_DON", "QUAN_LY_PHIEU_NHAP"),
            "NHAN_SU", List.of("QUAN_LY_NGUOI_DUNG"),
            "STAFF", List.of("QUAN_LY_SACH")
    );

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

        if (!ROLE_ADMIN.equals(nd.getNhom().getTenNhom())) {
            applyRolePermissions(nd, nd.getNhom().getTenNhom());
            return;
        }

        List<ChucNang> dsChucNang = chucNangRepo.findAll();

        for (ChucNang cn : dsChucNang) {

            if (phanQuyenRepo.countByNguoiDungIdAndChucNangId(nd.getId(), cn.getMaChucNang()) > 0) {
                continue;
            }

            PhanQuyen pq = new PhanQuyen();
            pq.setNguoiDung(nd);
            pq.setChucNang(cn);

            phanQuyenRepo.save(pq);
        }
    }

    @Override
    @Transactional
    public void addStaffPermissions(NguoiDung nd) {
        applyRolePermissions(nd, "STAFF");
    }

    @Override
    @Transactional
    public NguoiDung create(CreateUserRequest req) {

        if (repo.findByUsername(req.getTenDangNhap()).isPresent()) {
            throw new RuntimeException("Tài khoản đã tồn tại");
        }

        NhomNguoiDung nhom = getOrCreateNhom(req.getTenNhom());

        NguoiDung nd = new NguoiDung();

        nd.setUsername(req.getTenDangNhap());

        String hash = encoder.encode(req.getMatKhau());

        nd.setPassword(hash);

        nd.setSDT(req.getSdt());

        nd.setHoTen(req.getHoTen());

        nd.setNhom(nhom);
        nd.setEmail(req.getEmail());
        nd.setAvatar(req.getAvatar());
        nd.setCaLamViec(req.getCaLamViec());
        nd.setDiaChi(req.getDiaChi());
        nd.setLuongCoBan(req.getLuongCoBan());
        nd.setGhiChu(req.getGhiChu());

        NguoiDung saved = repo.save(nd);

        applyRolePermissions(saved, nhom.getTenNhom());

        return saved;
    }
    @Transactional
    @Override
    // Cập nhật thông tin người dùng và đồng bộ lại quyền theo chức vụ.
    public NguoiDung update(int id, CreateUserRequest req) {

        NguoiDung nd = repo.findById(id)
                .orElseThrow(() -> new AuthException("Người dùng không tồn tại"));

        if (!nd.getUsername().equals(req.getTenDangNhap())
                && repo.findByUsername(req.getTenDangNhap()).isPresent()) {

            throw new RuntimeException("Tài khoản đã tồn tại");
        }

        NhomNguoiDung nhom = getOrCreateNhom(req.getTenNhom());

        nd.setUsername(req.getTenDangNhap());

        if (req.getMatKhau() != null && !req.getMatKhau().isBlank()) {

            String hash = encoder.encode(req.getMatKhau());

            nd.setPassword(hash);
        }

        nd.setHoTen(req.getHoTen());

        nd.setSDT(req.getSdt());
        nd.setNhom(nhom);
        nd.setEmail(req.getEmail());
        nd.setAvatar(req.getAvatar());
        nd.setCaLamViec(req.getCaLamViec());
        nd.setDiaChi(req.getDiaChi());
        nd.setLuongCoBan(req.getLuongCoBan());
        nd.setGhiChu(req.getGhiChu());

        NguoiDung saved = repo.save(nd);
        applyRolePermissions(saved, nhom.getTenNhom());

        return saved;
    }
    @Transactional
    @Override
    public void delete(int id) {

        NguoiDung nd = repo.findById(id)
                .orElseThrow(() -> new AuthException("Người dùng không tồn tại"));

        phanQuyenRepo.deleteAllByNguoiDungId(nd.getId());

        repo.delete(nd);
    }

    private NhomNguoiDung getOrCreateNhom(String tenNhom) {
        String normalizedTenNhom = tenNhom == null || tenNhom.isBlank() ? "STAFF" : tenNhom.trim();
        return nhomRepo.findByTenNhom(normalizedTenNhom)
                .orElseGet(() -> {
                    NhomNguoiDung nhom = new NhomNguoiDung();
                    nhom.setTenNhom(normalizedTenNhom);
                    return nhomRepo.save(nhom);
                });
    }

    private void applyRolePermissions(NguoiDung nd, String tenNhom) {
        phanQuyenRepo.deleteAllByNguoiDungId(nd.getId());

        String normalizedRole = tenNhom == null || tenNhom.isBlank() ? "STAFF" : tenNhom.trim();
        if (ROLE_ADMIN.equals(normalizedRole)) {
            addAllPermissions(nd.getId());
            return;
        }

        List<String> permissionNames = ROLE_PERMISSION_NAMES.getOrDefault(normalizedRole, List.of());
        if (permissionNames.isEmpty()) {
            return;
        }

        List<ChucNang> permissions = chucNangRepo.findByTenChucNangIn(permissionNames);
        List<String> foundNames = permissions.stream()
                .map(ChucNang::getTenChucNang)
                .toList();
        List<String> missingNames = permissionNames.stream()
                .filter(name -> !foundNames.contains(name))
                .toList();

        if (!missingNames.isEmpty()) {
            throw new AuthException("Quyền chưa được khai báo trong hệ thống: " + missingNames);
        }

        List<Integer> permissionIds = permissions.stream()
                .map(ChucNang::getMaChucNang)
                .toList();
        addPermissionsByIds(nd, permissionIds);
    }

    private void addPermissionsByIds(NguoiDung nd, List<Integer> permissionIds) {
        List<ChucNang> permissions = chucNangRepo.findAllById(permissionIds);

        for (ChucNang cn : permissions) {
            PhanQuyen pq = new PhanQuyen();
            pq.setNguoiDung(nd);
            pq.setChucNang(cn);
            phanQuyenRepo.save(pq);
        }
    }

    @Transactional
    @Override
    public void updatePermissions(int id, List<Integer> permissionIds) {

        NguoiDung nd = repo.findById(id)
                .orElseThrow(() -> new AuthException("Người dùng không tồn tại"));
        applyRolePermissions(nd, nd.getNhom().getTenNhom());
    }





}
