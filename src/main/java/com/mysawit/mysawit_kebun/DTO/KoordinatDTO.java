package com.mysawit.mysawit_kebun.DTO;

import com.mysawit.mysawit_kebun.model.Koordinat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KoordinatDTO {
    private int x;
    private int y;

    public Koordinat toEntity() {
        return new Koordinat(this.x, this.y);
    }
}
