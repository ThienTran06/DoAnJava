package com.library.librarymanager.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdatePermissionsRequest {
    @NotEmpty(message = "Danh sach quyen khong duoc trong")
    private List<@Positive(message = "Permission id phai lon hon 0") Integer> permissionIds;
}
