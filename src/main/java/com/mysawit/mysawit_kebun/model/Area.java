package com.mysawit.mysawit_kebun.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Area {
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "bottom_left_x")),
            @AttributeOverride(name = "y", column = @Column(name = "bottom_left_y"))
    })
    private Koordinat bottomLeft;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "bottom_right_x")),
            @AttributeOverride(name = "y", column = @Column(name = "bottom_right_y"))
    })
    private Koordinat bottomRight;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "top_right_x")),
            @AttributeOverride(name = "y", column = @Column(name = "top_right_y"))
    })
    private Koordinat topRight;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "top_left_x")),
            @AttributeOverride(name = "y", column = @Column(name = "top_left_y"))
    })
    private Koordinat topLeft;
}
