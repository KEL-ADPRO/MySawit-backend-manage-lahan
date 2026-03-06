package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.model.Area;
import com.mysawit.mysawit_kebun.model.Koordinat;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class overlapCheckerTest {

    @BeforeEach
    public void setUp() {
        OverlapChecker overlapChecker = new OverlapChecker;
    }
    @Test
    public void checkOverlapTrue() {
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

        assertTrue(overlapChecker.check(area1, area2));
    }

    @Test
    public void checkOverlapFalse() {
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

        assertTrue(overlapChecker.check(area1, area2));
    }
}
