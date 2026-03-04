package com.mysawit.mysawit_kebun.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Koordinat {
    private int x;
    private int y;
}

