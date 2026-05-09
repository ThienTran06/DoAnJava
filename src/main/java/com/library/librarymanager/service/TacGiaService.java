package com.library.librarymanager.service;

import com.library.librarymanager.entity.TacGia;

import java.util.List;

public interface TacGiaService {
    List<TacGia> getAll();
    TacGia getById(int id);
    TacGia create(TacGia tacGia);
    TacGia updateById(int id,TacGia tacGia);
    void deleteById(int id);
}
