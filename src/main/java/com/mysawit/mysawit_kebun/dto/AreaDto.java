package com.mysawit.mysawit_kebun.dto;

import com.mysawit.mysawit_kebun.model.Area;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaDto {
    @NotNull(message = "Bottom-left coordinate is required")
    @Valid
    private KoordinatDto bottomLeft;

    @NotNull(message = "Bottom-right coordinate is required")
    @Valid
    private KoordinatDto bottomRight;

    @NotNull(message = "Top-right coordinate is required")
    @Valid
    private KoordinatDto topRight;

    @NotNull(message = "Top-left coordinate is required")
    @Valid
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
