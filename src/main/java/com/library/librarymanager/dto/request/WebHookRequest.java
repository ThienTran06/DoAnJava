package com.library.librarymanager.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter

public class WebHookRequest {
    private String content;
    private BigDecimal transferAmount;
    private String bank;
}