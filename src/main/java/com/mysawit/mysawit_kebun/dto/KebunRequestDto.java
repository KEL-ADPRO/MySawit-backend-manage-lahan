package com.mysawit.mysawit_kebun.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KebunRequestDto {
    @NotBlank(message = "Kebun name (nama) is required and cannot be empty")
    private String nama;

    @Positive(message = "Kebun area (luas) must be greater than 0")
    private double luas;

    @NotNull(message = "Area coordinates are required")
    @Valid
    private AreaDto area;
}
