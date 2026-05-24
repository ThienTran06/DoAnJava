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

        NhaXuatBan nhaXuatBan = nhaXuatBanRepository.findById(nhaXuatBanId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy nhà xuất bản")
                );

        List<TacGia> dsTacGia =
                tacGiaRepository.findAllById(tacGiaIds);

        if (dsTacGia.size() != tacGiaIds.size()) {
            throw new RuntimeException("Có tác giả không tồn tại");
        }



        sach.setTenSach(tenSach);
        sach.setGiaBan(giaBan);
        sach.setSoLuongTon(soLuongTon);
        sach.setNamXuatBan(namXuatBan);



        sach.setTheLoai(theLoai);
        sach.setNhaXuatBan(nhaXuatBan);
        sach.setDanhSachTacGia(dsTacGia);

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

        NhaXuatBan nhaXuatBan = nhaXuatBanRepository.findById(nhaXuatBanId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy nhà xuất bản")
                );

        List<TacGia> dsTacGia =
                tacGiaRepository.findAllById(tacGiaIds);

        if (dsTacGia.size() != tacGiaIds.size()) {
            throw new RuntimeException("Có tác giả không tồn tại");
        }

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
        sach.setNhaXuatBan(nhaXuatBan);
        sach.setDanhSachTacGia(dsTacGia);

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