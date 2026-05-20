package com.library.librarymanager.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Positive
@NotEmpty
@Valid
public class UpdatePermissionsRequest {
    private List<Integer> permissionIds;
}
