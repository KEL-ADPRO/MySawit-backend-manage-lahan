package com.mysawit.mysawit_kebun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KebunRequestDto {
    private String nama;
    private double luas;
    private AreaDto area;
}
