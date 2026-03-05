package com.mysawit.mysawit_kebun.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AreaTest {
    private Area area;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testAreaCreation() {
        Koordinat koordinat1 = new Koordinat(0, 0);
        Koordinat koordinat2 = new Koordinat(100, 0);
        Koordinat koordinat3 = new Koordinat(100, 100);
        Koordinat koordinat4 = new Koordinat(0, 100);
        area = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        assertEquals(koordinat1, area.getBottomLeft());
        assertEquals(koordinat2, area.getBottomRight());
        assertEquals(koordinat3, area.getTopRight());
        assertEquals(koordinat4, area.getTopLeft());
    }
}
