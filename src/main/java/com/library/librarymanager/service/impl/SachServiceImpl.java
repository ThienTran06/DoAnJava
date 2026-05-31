package com.library.librarymanager.service.impl;

import com.library.librarymanager.entity.NhaXuatBan;
import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.entity.TacGia;
import com.library.librarymanager.entity.TheLoai;
import com.library.librarymanager.repository.NhaXuatBanRepository;
import com.library.librarymanager.repository.SachRepository;
import com.library.librarymanager.repository.TacGiaRepository;
import com.library.librarymanager.repository.TheLoaiRepository;
import com.library.librarymanager.service.Interface.CloudinaryService;
import com.library.librarymanager.service.Interface.SachService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SachServiceImpl implements SachService {

    private final SachRepository sachRepository;
    private final TheLoaiRepository theLoaiRepository;
    private final TacGiaRepository tacGiaRepository;
    private final NhaXuatBanRepository nhaXuatBanRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public List<Sach> getAll() {
        return sachRepository.findAll();
    }

    @Override
    public Sach getById(int id) {

        return sachRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Không tìm thấy sách có id = " + id
                        )
                );
    }

    private boolean hasText(String text) {
        return text != null && !text.trim().isEmpty();
    }

    private NhaXuatBan resolveNhaXuatBan(Integer nhaXuatBanId, String tenNhaXuatBanMoi) {
        if (nhaXuatBanId != null) {
            return nhaXuatBanRepository.findById(nhaXuatBanId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà xuất bản"));
        }

        if (hasText(tenNhaXuatBanMoi)) {
            String ten = tenNhaXuatBanMoi.trim();
            NhaXuatBan nhaXuatBan = nhaXuatBanRepository.findByTenNXBIgnoreCase(ten);
            if (nhaXuatBan == null) {
                nhaXuatBan = new NhaXuatBan();
                nhaXuatBan.setTenNXB(ten);
                nhaXuatBan = nhaXuatBanRepository.save(nhaXuatBan);
            }
            return nhaXuatBan;
        }

        throw new RuntimeException("Vui lòng chọn hoặc nhập nhà xuất bản");
    }

    private TacGia resolveTacGia(List<Integer> tacGiaIds, String tenTacGiaMoi) {
        if (tacGiaIds != null && !tacGiaIds.isEmpty()) {
            return tacGiaRepository.findById(tacGiaIds.get(0))
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tác giả"));
        }

        if (hasText(tenTacGiaMoi)) {
            String ten = tenTacGiaMoi.trim();
            TacGia tacGia = tacGiaRepository.findByHoTenIgnoreCase(ten);
            if (tacGia == null) {
                tacGia = new TacGia();
                tacGia.setHoTen(ten);
                tacGia = tacGiaRepository.save(tacGia);
            }
            return tacGia;
        }

        return null;
    }

    @Override
    @Transactional
    public Sach create(
            String tenSach,
            BigDecimal giaBan,
            Integer soLuongTon,
            Integer namXuatBan,
            Integer theLoaiId,
            Integer nhaXuatBanId,
            List<Integer> tacGiaIds,
            String tenNhaXuatBanMoi,
            String tenTacGiaMoi,
            MultipartFile hinhAnh
    ) {

        Sach sach = new Sach();
        if (hinhAnh != null && !hinhAnh.isEmpty()) {

            if (!hinhAnh.getContentType().startsWith("image/")) {
                throw new RuntimeException("File phải là ảnh");
            }

            if (hinhAnh.getSize() > 5 * 1024 * 1024) {
                throw new RuntimeException("Ảnh vượt quá 5MB");
            }

             String url = cloudinaryService.uploadFile(hinhAnh);
            sach.setHinhAnh(url);

        }

        TheLoai theLoai = theLoaiRepository.findById(theLoaiId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy thể loại")
                );

        NhaXuatBan nhaXuatBan = resolveNhaXuatBan(nhaXuatBanId, tenNhaXuatBanMoi);
        TacGia tacGia = resolveTacGia(tacGiaIds, tenTacGiaMoi);

        sach.setTenSach(tenSach);
        sach.setGiaBan(giaBan);
        sach.setSoLuongTon(soLuongTon);
        sach.setNamXuatBan(namXuatBan);



        sach.setTheLoai(theLoai);
        sach.setTacGia(tacGia);
        sach.setNhaXuatBan(nhaXuatBan);

        return sachRepository.save(sach);
    }

    @Override
    @Transactional
    public Sach updateById(
            int id,
            String tenSach,
            BigDecimal giaBan,
            Integer soLuongTon,
            Integer namXuatBan,
            Integer theLoaiId,
            Integer nhaXuatBanId,
            List<Integer> tacGiaIds,
            String tenNhaXuatBanMoi,
            String tenTacGiaMoi,
            MultipartFile hinhAnh
    ) {

        Sach sach = sachRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Không tìm thấy sách có id = " + id
                        )
                );

        TheLoai theLoai = theLoaiRepository.findById(theLoaiId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy thể loại")
                );

        NhaXuatBan nhaXuatBan = resolveNhaXuatBan(nhaXuatBanId, tenNhaXuatBanMoi);
        TacGia tacGia = resolveTacGia(tacGiaIds, tenTacGiaMoi);

        sach.setTenSach(tenSach);
        sach.setGiaBan(giaBan);
        sach.setSoLuongTon(soLuongTon);
        sach.setNamXuatBan(namXuatBan);

        if (hinhAnh != null && !hinhAnh.isEmpty()) {

            if (!hinhAnh.getContentType().startsWith("image/")) {
                throw new RuntimeException("File phải là ảnh");
            }

            if (hinhAnh.getSize() > 5 * 1024 * 1024) {
                throw new RuntimeException("Ảnh vượt quá 5MB");
            }

            String url = cloudinaryService.uploadFile(hinhAnh);

            sach.setHinhAnh(url);
        }
        sach.setTheLoai(theLoai);
        sach.setTacGia(tacGia);
        sach.setNhaXuatBan(nhaXuatBan);

        return sachRepository.save(sach);
    }

    @Override
    @Transactional
    public void deleteById(int id) {

        Sach sach = sachRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Không tìm thấy sách có id = " + id
                        )
                );

        sachRepository.delete(sach);
    }

    @Override
    public List<Sach> search(
            String tenSach,
            String tenTheLoai,
            String tenTacGia,
            Integer namXuatBan
    ) {

        return sachRepository.search(
                tenSach,
                tenTheLoai,
                tenTacGia,
                namXuatBan
        );
    }
}