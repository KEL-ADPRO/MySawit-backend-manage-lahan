package com.mysawit.mysawit_kebun.DTO;

import com.mysawit.mysawit_kebun.model.Area;
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

    public Area toEntity() {
        return new Area(
                this.bottomLeft.toEntity(),
                this.bottomRight.toEntity(),
                this.topRight.toEntity(),
                this.topLeft.toEntity()
        );
    }
}
