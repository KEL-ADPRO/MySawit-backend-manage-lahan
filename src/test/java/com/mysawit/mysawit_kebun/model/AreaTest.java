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

    @Test
    public void testGetCalculatedAreaSquare() {
        Koordinat p1 = new Koordinat(0, 0);
        Koordinat p2 = new Koordinat(100, 0);
        Koordinat p3 = new Koordinat(100, 100);
        Koordinat p4 = new Koordinat(0, 100);
        Area squareArea = new Area(p1, p2, p3, p4);

        assertEquals(10000.0, squareArea.getCalculatedArea());
    }

    @Test
    public void testGetCalculatedAreaTrapezoid() {
        Koordinat p1 = new Koordinat(0, 0);
        Koordinat p2 = new Koordinat(100, 0);
        Koordinat p3 = new Koordinat(80, 50);
        Koordinat p4 = new Koordinat(20, 50);
        Area trapezoid = new Area(p1, p2, p3, p4);

        // Trapezoid area: 0.5 * (a + b) * h = 0.5 * (100 + 60) * 50 = 4000.0
        assertEquals(4000.0, trapezoid.getCalculatedArea());
    }

    @Test
    public void testGetCalculatedAreaWithNullCoordinates() {
        Area nullArea = new Area(null, null, null, null);
        assertEquals(0.0, nullArea.getCalculatedArea());
    }
}
