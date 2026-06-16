package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.HoaDonRequest;
import com.library.librarymanager.dto.request.UpdateHoaDonRequest;
import com.library.librarymanager.dto.response.HoaDonResponse;
import com.library.librarymanager.dto.response.ThongKeHoaDonResponse;
import com.library.librarymanager.entity.HoaDon;
import com.library.librarymanager.service.Interface.HoaDonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('QUAN_LY_HOA_DON')")
public class HoaDonController {
    private final HoaDonService hoaDonService;

    @GetMapping(params = {"page", "size"})
    Page<HoaDon> getPage(
            @RequestParam(required = false, name = "ma") String ma,
            @RequestParam(required = false, name = "ngay") LocalDate ngay,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return hoaDonService.getAll(parseId(ma), ngay, page, size);
    }

    @GetMapping
    List<HoaDon> getAll(){return hoaDonService.getAll();}
    @GetMapping("/{id}")
    HoaDon getById(@PathVariable int id){return hoaDonService.getById(id);}
    @PostMapping
    public HoaDonResponse create(@Valid @RequestBody HoaDonRequest req) {

        HoaDon hd = hoaDonService.create(req);

        String qrUrl =
                "https://img.vietqr.io/image/MB-0393107717-compact2.png"
                        + "?amount=" + hd.getTongTien()
                        + "&addInfo=HD" + hd.getId();

        HoaDonResponse res = new HoaDonResponse();
        res.setId(hd.getId());
        res.setTongTien(hd.getTongTien());
        res.setTienGiamGia(hd.getTienGiamGia());
        res.setMaGiamGia(hd.getMaGiamGia() == null ? null : hd.getMaGiamGia().getMa());
        res.setTrangThai(hd.getTrangThai());
        res.setQrUrl(qrUrl);

        return res;
    }
    @PutMapping("/{id}")
    HoaDon updateById(@PathVariable int id, @Valid @RequestBody UpdateHoaDonRequest request){return hoaDonService.updateById(id,request);}
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable int id){hoaDonService.deleteById(id);}
    @PutMapping("/{id}/huy_don")
    void huyHoaDon(@PathVariable int id) {
        hoaDonService.huyHoaDon(id);
    }
    @PutMapping("/{id}/chi-tiet")
    HoaDon updateChiTiet(@PathVariable int id, @Valid @RequestBody HoaDonRequest request) {
        return hoaDonService.updateChiTiet(id, request);
    }

    @GetMapping("/thong_ke")
    ThongKeHoaDonResponse getThongKe() {
        return hoaDonService.getThongKe();
    }

    private Integer parseId(String value) {
        if (value == null || value.isBlank()) return null;
        String digits = value.replaceAll("\\D", "");
        if (digits.isBlank()) return null;
        return Integer.parseInt(digits);
    }
    @PutMapping("/{id}/thanh-toan-tien-mat")
    public ResponseEntity<?> thanhToanTienMat(@PathVariable int id) {
        hoaDonService.thanhToanTienMat(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}/Pending")
    public ResponseEntity<Void> setPendingStatus(@PathVariable int id) {
        hoaDonService.setPendingStatus(id);
        return ResponseEntity.ok().build();
    }
}
