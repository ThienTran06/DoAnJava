package com.library.librarymanager.service.Interface;

import com.library.librarymanager.dto.request.CreateUserRequest;
import com.library.librarymanager.entity.NguoiDung;

import java.util.List;

public interface NguoiDungService {
    public List<NguoiDung> getAll();
    public NguoiDung getById(int id);
    public void addAllPermissions(int id);
    public void addStaffPermissions(NguoiDung nd);
    public NguoiDung create(CreateUserRequest req);
    public NguoiDung update(int id, CreateUserRequest req);
    public void delete(int id);
}
