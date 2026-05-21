package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.model.Area;
import com.mysawit.mysawit_kebun.model.Koordinat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OverlapCheckerTest {

    private OverlapChecker overlapChecker;

    @BeforeEach
    void setUp() {
        overlapChecker = new OverlapChecker();
    }

    @Test
    void checkOverlapTrue() {
        Koordinat koordinat1 = new Koordinat(0, 0);
        Koordinat koordinat2 = new Koordinat(100, 0);
        Koordinat koordinat3 = new Koordinat(100, 100);
        Koordinat koordinat4 = new Koordinat(0, 100);
        Area area1 = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        koordinat1 = new Koordinat(0, 0);
        koordinat2 = new Koordinat(100, 0);
        koordinat3 = new Koordinat(100, 100);
        koordinat4 = new Koordinat(0, 100);
        Area area2 = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        assertTrue(overlapChecker.checkOverlap(area1, area2));
    }

    @Test
    void checkOverlapFalse() {
        Koordinat koordinat1 = new Koordinat(0, 0);
        Koordinat koordinat2 = new Koordinat(100, 0);
        Koordinat koordinat3 = new Koordinat(100, 100);
        Koordinat koordinat4 = new Koordinat(0, 100);
        Area area1 = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        koordinat1 = new Koordinat(100, 0);
        koordinat2 = new Koordinat(200, 0);
        koordinat3 = new Koordinat(200, 100);
        koordinat4 = new Koordinat(100, 100);
        Area area2 = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        assertFalse(overlapChecker.checkOverlap(area1, area2));
    }

    @Test
    void checkOverlapRotatedTrue() {
        // Area 1: Axis-aligned square from 0 to 10
        Area area1 = new Area(
                new Koordinat(0, 0),
                new Koordinat(10, 0),
                new Koordinat(10, 10),
                new Koordinat(0, 10)
        );

        // Area 2: Diamond shape rotated by 45 degrees, centered around (8, 5)
        // Vertices: (8, 1), (12, 5), (8, 9), (4, 5)
        // This overlaps with Area 1 in the region around X in [4, 10]
        Area area2 = new Area(
                new Koordinat(8, 1),
                new Koordinat(12, 5),
                new Koordinat(8, 9),
                new Koordinat(4, 5)
        );

        assertTrue(overlapChecker.checkOverlap(area1, area2));
    }

    @Test
    void checkOverlapRotatedFalse() {
        // Area 1: Axis-aligned square from 0 to 10
        Area area1 = new Area(
                new Koordinat(0, 0),
                new Koordinat(10, 0),
                new Koordinat(10, 10),
                new Koordinat(0, 10)
        );

        // Area 2: Rotated shape completely outside Area 1
        // Centered around (18, 5) -> Vertices: (18, 1), (22, 5), (18, 9), (14, 5)
        // No overlap should be detected
        Area area2 = new Area(
                new Koordinat(18, 1),
                new Koordinat(22, 5),
                new Koordinat(18, 9),
                new Koordinat(14, 5)
        );

        assertFalse(overlapChecker.checkOverlap(area1, area2));
    }

    @Test
    void checkOverlapSkewedTrue() {
        // Skewed trapezoid Area 1
        Area area1 = new Area(
                new Koordinat(0, 0),
                new Koordinat(15, 2),
                new Koordinat(10, 10),
                new Koordinat(2, 8)
        );

        // Intersecting skewed trapezoid Area 2
        Area area2 = new Area(
                new Koordinat(8, 5),
                new Koordinat(20, 4),
                new Koordinat(18, 15),
                new Koordinat(7, 12)
        );

        assertTrue(overlapChecker.checkOverlap(area1, area2));
    }

    @Test
    void checkOverlapSkewedFalse() {
        // Skewed trapezoid Area 1
        Area area1 = new Area(
                new Koordinat(0, 0),
                new Koordinat(15, 2),
                new Koordinat(10, 10),
                new Koordinat(2, 8)
        );

        // Non-overlapping skewed trapezoid Area 2
        Area area2 = new Area(
                new Koordinat(16, 5),
                new Koordinat(25, 4),
                new Koordinat(23, 15),
                new Koordinat(15, 12)
        );

        assertFalse(overlapChecker.checkOverlap(area1, area2));
    }
}
