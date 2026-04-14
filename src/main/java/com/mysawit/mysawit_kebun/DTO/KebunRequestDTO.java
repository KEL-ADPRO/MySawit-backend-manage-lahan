package com.mysawit.mysawit_kebun.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KebunRequestDTO {
    private String nama;
    private double luas;
    private AreaDTO area;
}
