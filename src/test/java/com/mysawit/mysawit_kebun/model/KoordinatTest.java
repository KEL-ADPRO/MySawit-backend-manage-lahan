package com.mysawit.mysawit_kebun.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KoordinatTest {
    Koordinat koordinat;

    @BeforeEach
    public void setUp() {
        koordinat = new Koordinat();
        this.koordinat.setX(200);
        this.koordinat.setY(150);
    }

    @Test
    public void testGetter() {
        assertEquals(200, koordinat.getX());
        assertEquals(150, koordinat.getY());
    }
}
