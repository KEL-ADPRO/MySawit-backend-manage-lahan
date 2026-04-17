package com.mysawit.mysawit_kebun.dto;

import com.mysawit.mysawit_kebun.model.Area;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaDto {
    private KoordinatDto bottomLeft;
    private KoordinatDto bottomRight;
    private KoordinatDto topRight;
    private KoordinatDto topLeft;

    public Area toEntity() {
        return new Area(
                this.bottomLeft.toEntity(),
                this.bottomRight.toEntity(),
                this.topRight.toEntity(),
                this.topLeft.toEntity()
        );
    }
}
