package com.library.librarymanager.service.impl;

import com.library.librarymanager.dto.response.SachTonKhoResponse;
import com.library.librarymanager.entity.Sach;
import com.library.librarymanager.repository.SachRepository;
import com.library.librarymanager.service.Interface.CloudinaryService;
import com.library.librarymanager.service.Interface.SachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SachServiceImpl implements SachService
{
    private final SachRepository sachRepository;
    private final CloudinaryService cloudinaryService;
    @Override
    public List<Sach> getAll() {
        return sachRepository.findAll();
    }

    @Override
    public Sach getById(int id) {
        return sachRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy sách có id = "+id));
    }

    @Override
    public Sach create(Sach sach) {
        return sachRepository.save(sach);
    }

    @Override
    public Sach create(Sach sach, MultipartFile fileAnh) {
        String imageUrl = uploadImageIfPresent(fileAnh);
        if (imageUrl != null) {
            sach.setHinhAnh(imageUrl);
        }
        return sachRepository.save(sach);
    }

    @Override
    public Sach updateById(int id, Sach sach) {
        Sach res = sachRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy sách có id = "+id));
        res.setTenSach(sach.getTenSach());
        if (sach.getHinhAnh() != null) {
            res.setHinhAnh(sach.getHinhAnh());
        }
        res.setGiaBan(sach.getGiaBan());
        res.setNamXuatBan(sach.getNamXuatBan());
        res.setNhaXuatBan(sach.getNhaXuatBan());
        res.setSoLuongTon(sach.getSoLuongTon());
        res.setDanhSachTacGia(sach.getDanhSachTacGia());
        sachRepository.save(res);
        return res;
    }

    @Override
    public Sach updateById(int id, Sach sach, MultipartFile fileAnh) {
        String imageUrl = uploadImageIfPresent(fileAnh);
        if (imageUrl != null) {
            sach.setHinhAnh(imageUrl);
        }
        return updateById(id, sach);
    }

    @Override
    public void deleteById(int id) {
        sachRepository.deleteById(id);
    }
    @Override
    public List<Sach> search(String tenSach, String tenTheLoai, String tenTacGia, Integer namXuatBan) {
        return sachRepository.search(tenSach, tenTheLoai, tenTacGia, namXuatBan);
    }
    @Override
    public List<SachTonKhoResponse>getStockByName(String tenSach){
        return sachRepository.tonKhoTheoTen(tenSach);
    }

    @Override
    public List<SachTonKhoResponse> getTonKhoNhieuNhat() {
        return sachRepository.tonKhoNhieuNhat();
    }
    @Override
    public List<SachTonKhoResponse> getTonKhoIt(){
        return sachRepository.tonKhoIt();
    }

    @Override
    public Integer getTongSoLuongTon(){
        return sachRepository.getTongSoLuongTon();
    }

    private String uploadImageIfPresent(MultipartFile fileAnh) {
        if (fileAnh == null || fileAnh.isEmpty()) {
            return null;
        }
        String contentType = fileAnh.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File phai la anh");
        }
        if (fileAnh.getSize() > 5 * 1024 * 1024) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anh vuot qua 5MB");
        }
        return cloudinaryService.uploadFile(fileAnh);
    }

}
