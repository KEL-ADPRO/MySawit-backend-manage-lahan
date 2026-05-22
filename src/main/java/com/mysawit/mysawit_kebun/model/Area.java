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

    public double getCalculatedArea() {
        if (bottomLeft == null || bottomRight == null || topRight == null || topLeft == null) {
            return 0.0;
        }
        double sum = ((double) bottomLeft.getX() * bottomRight.getY() - (double) bottomLeft.getY() * bottomRight.getX())
                   + ((double) bottomRight.getX() * topRight.getY() - (double) bottomRight.getY() * topRight.getX())
                   + ((double) topRight.getX() * topLeft.getY() - (double) topRight.getY() * topLeft.getX())
                   + ((double) topLeft.getX() * bottomLeft.getY() - (double) topLeft.getY() * bottomLeft.getX());
        return 0.5 * Math.abs(sum);
    }
}
