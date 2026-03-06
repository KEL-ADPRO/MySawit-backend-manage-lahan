package com.mysawit.mysawit_kebun.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KebunTest {
    Kebun kebun;
    UUID uuid;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testKebunCreation() {
        Koordinat koordinat1 = new Koordinat(0, 0);
        Koordinat koordinat2 = new Koordinat(100, 0);
        Koordinat koordinat3 = new Koordinat(100, 100);
        Koordinat koordinat4 = new Koordinat(0, 100);
        Area area = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        kebun = new Kebun();
        uuid = UUID.fromString("aa558a9a-1a39-460a-8860-71aa6aa63aa6");
        this.kebun.setId(uuid);
        this.kebun.setNama("Kebun A");
        this.kebun.setLuas(100);
        this.kebun.setArea(area);

        assertEquals(uuid, kebun.getId());
        assertEquals("Kebun A", kebun.getNama());
        assertEquals(100, kebun.getLuas());
        assertEquals(area, kebun.getArea());
    }
}
