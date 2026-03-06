package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.model.Area;
import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.model.Koordinat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class KebunServiceTest {
    private KebunService kebunService;

    @BeforeEach
    public void setUp() {
        Koordinat koordinat1 = new Koordinat(0, 0);
        Koordinat koordinat2 = new Koordinat(100, 0);
        Koordinat koordinat3 = new Koordinat(100, 100);
        Koordinat koordinat4 = new Koordinat(0, 100);
        Area area = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        Kebun kebun1 = new Kebun();
        UUID uuid1 = UUID.fromString("aa558a9a-1a39-460a-8860-71aa6aa63aa6");
        kebun1.setId(uuid1);
        kebun1.setNama("Kebun1");
        kebun1.setLuas(100);
        kebun1.setArea(area);

        koordinat1 = new Koordinat(100, 0);
        koordinat2 = new Koordinat(200, 0);
        koordinat3 = new Koordinat(200, 100);
        koordinat4 = new Koordinat(100, 100);
        area = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        Kebun kebun2 = new Kebun();
        UUID uuid2 = UUID.fromString("bb558b9b-1b39-460b-8860-71bb6bb63bb6");
        kebun2.setId(uuid2);
        kebun2.setNama("Kebun2");
        kebun2.setLuas(100);
        kebun2.setArea(area);

        kebunService = new KebunServiceImpl();
        kebunService.createKebun(kebun1);
        kebunService.createKebun(kebun2);
    }

    @Test
    public void testCreateKebun() {
        Koordinat koordinat1 = new Koordinat(200, 0);
        Koordinat koordinat2 = new Koordinat(300, 0);
        Koordinat koordinat3 = new Koordinat(300, 100);
        Koordinat koordinat4 = new Koordinat(200, 100);
        Area area = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        Kebun kebun = new Kebun();
        UUID uuid = UUID.fromString("cc558c9c-1c39-460c-8860-71cc6cc63cc6");
        kebun.setId(uuid);
        kebun.setNama("Kebun3");
        kebun.setLuas(100);
        kebun.setArea(area);

        Kebun createdKebun = kebunService.createKebun(kebun);
        assertEquals("Kebun3", createdKebun.getNama());
    }

    @Test
    public void testCreateKebunOverlap() {
        Koordinat koordinat1 = new Koordinat(50, 50);
        Koordinat koordinat2 = new Koordinat(150, 50);
        Koordinat koordinat3 = new Koordinat(150, 150);
        Koordinat koordinat4 = new Koordinat(50, 150);
        Area area = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        Kebun kebun = new Kebun();
        UUID uuid = UUID.fromString("dd558d9d-1d39-460d-8860-71dd6dd63dd6");
        kebun.setId(uuid);
        kebun.setNama("Kebun4");
        kebun.setLuas(100);
        kebun.setArea(area);

        Kebun createdKebun = kebunService.createKebun(kebun);
        assertNull(createdKebun);
    }

    @Test
    public void testFindAllKebun() {
        List<Kebun> allKebun = kebunService.findAllKebun();
        assertEquals(2, allKebun.size());
    }

    @Test
    public void testFindKebunById() {
        Kebun kebun = kebunService.findById("aa558a9a-1a39-460a-8860-71aa6aa63aa6");
        assertEquals("Kebun1", kebun.getNama());
    }

    @Test
    public void testFindKebunByIdIfNotExist() {
        Kebun kebun = kebunService.findById("dd558d9d-1d39-460d-8860-71dd6dd63dd6");
        assertNull(kebun);
    }

    @Test
    public void testFindKebunByName() {
        Kebun kebun = kebunService.findByName("Kebun2");
        assertEquals("bb558b9b-1b39-460b-8860-71bb6bb63bb6", kebun.getId().toString());
    }

    @Test
    public void testFindKebunByNameIfNotExist() {
        Kebun kebun = kebunService.findByName("Kebun4");
        assertNull(kebun);
    }

    @Test
    public void testDeleteKebunById() {
        Kebun kebun = kebunService.findById("aa558a9a-1a39-460a-8860-71aa6aa63aa6");
        kebunService.deleteKebunById(kebun);
        List<Kebun> allKebun = kebunService.findAllKebun();
        assertEquals(1, allKebun.size());
    }

    @Test
    public void testDeleteKebunByIdIfNotExist() {
        Kebun kebun = new Kebun();
        UUID uuid = UUID.fromString("dd558d9d-1d39-460d-8860-71dd6dd63dd6");
        kebun.setId(uuid);
        kebun.setNama("Kebun4");
        kebun.setLuas(100);
        Area area = new Area(new Koordinat(300, 0), new Koordinat(400, 0), new Koordinat(400, 100), new Koordinat(300, 100));
        kebun.setArea(area);

        kebunService.deleteKebunById(kebun);
        List<Kebun> allKebun = kebunService.findAllKebun();
        assertEquals(2, allKebun.size());
    }

    @Test
    public void testDeleteKebunByIdIfEmpty() {
        List<Kebun> allKebun = kebunService.findAllKebun();
        for (Kebun kebun : allKebun) {
            kebunService.deleteKebunById(kebun);
        }

        Kebun kebun = new Kebun();
        UUID uuid = UUID.fromString("dd558d9d-1d39-460d-8860-71dd6dd63dd6");
        kebun.setId(uuid);
        kebun.setNama("Kebun4");
        kebun.setLuas(100);
        Area area = new Area(new Koordinat(300, 0), new Koordinat(400, 0), new Koordinat(400, 100), new Koordinat(300, 100));
        kebun.setArea(area);

        kebunService.deleteKebunById(kebun);
        allKebun = kebunService.findAllKebun();
        assertEquals(0, allKebun.size());
    }
}
