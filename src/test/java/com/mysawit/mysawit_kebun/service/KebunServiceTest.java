package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.model.Area;
import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.model.Koordinat;
import com.mysawit.mysawit_kebun.repository.KebunRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KebunServiceTest {
    @Mock
    private KebunRepository kebunRepository;
    @Mock
    private OverlapChecker overlapChecker;
    @InjectMocks
    private KebunServiceImpl kebunService;

    private Kebun kebun1;
    private Kebun kebun2;
    private List<Kebun> kebunList;

    @BeforeEach
    public void setUp() {
        Koordinat koordinat1 = new Koordinat(0, 0);
        Koordinat koordinat2 = new Koordinat(100, 0);
        Koordinat koordinat3 = new Koordinat(100, 100);
        Koordinat koordinat4 = new Koordinat(0, 100);
        Area area1 = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        kebun1 = new Kebun();
        UUID uuid1 = UUID.fromString("aa558a9a-1a39-460a-8860-71aa6aa63aa6");
        kebun1.setId(uuid1);
        kebun1.setNama("Kebun1");
        kebun1.setLuas(1);
        kebun1.setArea(area1);

        Koordinat koordinat5 = new Koordinat(100, 0);
        Koordinat koordinat6 = new Koordinat(200, 0);
        Koordinat koordinat7 = new Koordinat(200, 100);
        Koordinat koordinat8 = new Koordinat(100, 100);
        Area area2 = new Area(koordinat5, koordinat6, koordinat7, koordinat8);

        kebun2 = new Kebun();
        UUID uuid2 = UUID.fromString("bb558b9b-1b39-460b-8860-71bb6bb63bb6");
        kebun2.setId(uuid2);
        kebun2.setNama("Kebun2");
        kebun2.setLuas(1);
        kebun2.setArea(area2);

        kebunList = Arrays.asList(kebun1, kebun2);
    }

    @Test
    public void testCreateKebunSuccess() {
        Koordinat koordinat1 = new Koordinat(200, 0);
        Koordinat koordinat2 = new Koordinat(300, 0);
        Koordinat koordinat3 = new Koordinat(300, 200);
        Koordinat koordinat4 = new Koordinat(200, 200);
        Area area = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        Kebun kebun = new Kebun();
        UUID uuid = UUID.fromString("cc558c9c-1c39-460c-8860-71cc6cc63cc6");
        kebun.setId(uuid);
        kebun.setNama("Kebun3");
        kebun.setLuas(100);
        kebun.setArea(area);

        when(kebunRepository.findAll()).thenReturn(kebunList);
        when(overlapChecker.checkOverlap(area, kebun1.getArea())).thenReturn(false);
        when(overlapChecker.checkOverlap(area, kebun2.getArea())).thenReturn(false);
        when(kebunRepository.save(kebun)).thenReturn(kebun);

        Kebun createdKebun = kebunService.createKebun(kebun);
        assertEquals("Kebun3", createdKebun.getNama());
    }

    @Test
    public void testCreateKebunSameName() {
        Koordinat koordinat1 = new Koordinat(200, 0);
        Koordinat koordinat2 = new Koordinat(300, 0);
        Koordinat koordinat3 = new Koordinat(300, 200);
        Koordinat koordinat4 = new Koordinat(200, 200);
        Area area = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        Kebun kebun = new Kebun();
        UUID uuid = UUID.fromString("cc558c9c-1c39-460c-8860-71cc6cc63cc6");
        kebun.setId(uuid);
        kebun.setNama("Kebun1");
        kebun.setLuas(100);
        kebun.setArea(area);

        when(kebunRepository.existsByNama("Kebun1")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kebunService.createKebun(kebun);
        });

        assertEquals("Kebun with name Kebun1 already exists.", exception.getMessage());
    }

    @Test
    public void testCreateKebunOverlapped() {
        Koordinat koordinat1 = new Koordinat(50, 50);
        Koordinat koordinat2 = new Koordinat(150, 50);
        Koordinat koordinat3 = new Koordinat(150, 150);
        Koordinat koordinat4 = new Koordinat(50, 150);
        Area area = new Area(koordinat1, koordinat2, koordinat3, koordinat4);

        Kebun kebun = new Kebun();
        UUID uuid = UUID.fromString("dd558d9d-1d39-460d-8860-71dd6dd63dd6");
        kebun.setId(uuid);
        kebun.setNama("Kebun4");
        kebun.setLuas(2);
        kebun.setArea(area);

        when(kebunRepository.findAll()).thenReturn(kebunList);
        when(overlapChecker.checkOverlap(any(), any())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kebunService.createKebun(kebun);
        });

        assertEquals("Kebun overlaps with an existing kebun.", exception.getMessage());
    }

    @Test
    public void testFindAllKebun() {
        when(kebunRepository.findAll()).thenReturn(kebunList);

        List<Kebun> allKebun = kebunService.findAllKebun();
        assertEquals(2, allKebun.size());
    }

    @Test
    public void testFindKebunById() {
        UUID uuid = UUID.fromString("aa558a9a-1a39-460a-8860-71aa6aa63aa6");
        when(kebunRepository.findById(uuid)).thenReturn(Optional.of(kebun1));

        Kebun kebun = kebunService.findById("aa558a9a-1a39-460a-8860-71aa6aa63aa6");
        assertEquals("Kebun1", kebun.getNama());
    }

    @Test
    public void testFindKebunByIdIfNotExist() {
        UUID uuid = UUID.fromString("dd558d9d-1d39-460d-8860-71dd6dd63dd6");
        when(kebunRepository.findById(uuid)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kebunService.findById(uuid.toString());
        });
        assertEquals("Kebun with ID " + uuid.toString() + " not found.", exception.getMessage());
    }

    @Test
    public void testFindKebunByName() {
        when(kebunRepository.findByNama("Kebun2")).thenReturn(Optional.of(kebun2));

        Kebun kebun = kebunService.findByName("Kebun2");
        assertEquals("bb558b9b-1b39-460b-8860-71bb6bb63bb6", kebun.getId().toString());
    }

    @Test
    public void testFindKebunByNameIfNotExist() {
        when(kebunRepository.findByNama("Kebun4")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kebunService.findByName("Kebun4");
        });
        assertEquals("Kebun with name Kebun4 not found.", exception.getMessage());
    }

    @Test
    public void testDeleteKebunById() {
        UUID uuid = UUID.fromString("aa558a9a-1a39-460a-8860-71aa6aa63aa6");

        when(kebunRepository.findById(uuid)).thenReturn(Optional.of(kebun1));
        when(kebunRepository.findAll()).thenReturn(Collections.singletonList(kebun2));

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

        when(kebunRepository.findAll()).thenReturn(kebunList);

        kebunService.deleteKebunById(kebun);
        List<Kebun> allKebun = kebunService.findAllKebun();
        assertEquals(2, allKebun.size());
    }

    @Test
    public void testDeleteKebunByIdIfEmpty() {
        when(kebunRepository.findAll())
                .thenReturn(kebunList)
                .thenReturn(Collections.emptyList());

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
