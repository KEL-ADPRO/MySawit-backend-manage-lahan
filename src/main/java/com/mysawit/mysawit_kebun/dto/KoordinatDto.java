package com.mysawit.mysawit_kebun.dto;

import com.mysawit.mysawit_kebun.model.Koordinat;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KoordinatDto {
    @Min(value = 0, message = "X coordinate must be >= 0")
    private int x;

    @Min(value = 0, message = "Y coordinate must be >= 0")
    private int y;

    public Koordinat toEntity() {
        return new Koordinat(this.x, this.y);
    }
}
