package com.library.librarymanager.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UpdatePermissionsRequest {
    private List<Integer> permissionIds;
}
