package com.mysawit.mysawit_kebun.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaDTO {
    private KoordinatDTO bottomLeft;
    private KoordinatDTO bottomRight;
    private KoordinatDTO topRight;
    private KoordinatDTO topLeft;
}
