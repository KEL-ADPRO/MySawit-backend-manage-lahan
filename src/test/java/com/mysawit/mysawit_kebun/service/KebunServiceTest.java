package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.DTO.AreaDTO;
import com.mysawit.mysawit_kebun.DTO.KebunRequestDTO;
import com.mysawit.mysawit_kebun.DTO.KoordinatDTO;
import com.mysawit.mysawit_kebun.event.MandorAssignmentEvent;
import com.mysawit.mysawit_kebun.event.MandorRemovalEvent;
import com.mysawit.mysawit_kebun.event.SupirAssignmentEvent;
import com.mysawit.mysawit_kebun.event.SupirRemovalEvent;
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
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KebunServiceTest {
    @Mock
    private KebunRepository kebunRepository;
    @Mock
    private OverlapChecker overlapChecker;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
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
        KoordinatDTO koordinat1 = new KoordinatDTO(200, 0);
        KoordinatDTO koordinat2 = new KoordinatDTO(300, 0);
        KoordinatDTO koordinat3 = new KoordinatDTO(300, 200);
        KoordinatDTO koordinat4 = new KoordinatDTO(200, 200);
        AreaDTO area = new AreaDTO(koordinat1, koordinat2, koordinat3, koordinat4);

        KebunRequestDTO requestDTO = new KebunRequestDTO();
        requestDTO.setNama("Kebun3");
        requestDTO.setLuas(100);
        requestDTO.setArea(area);

        Kebun expectedSavedKebun = new Kebun();
        expectedSavedKebun.setNama("Kebun3");

        when(kebunRepository.findAll()).thenReturn(kebunList);
        when(overlapChecker.checkOverlap(any(Area.class), any(Area.class))).thenReturn(false);
        when(kebunRepository.save(any(Kebun.class))).thenReturn(expectedSavedKebun);

        Kebun createdKebun = kebunService.createKebun(requestDTO);
        assertEquals("Kebun3", createdKebun.getNama());
    }

    @Test
    public void testCreateKebunSameName() {
        KebunRequestDTO requestDTO = new KebunRequestDTO();
        requestDTO.setNama("Kebun1");

        when(kebunRepository.existsByNama("Kebun1")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kebunService.createKebun(requestDTO);
        });

        assertEquals("Kebun with name Kebun1 already exists.", exception.getMessage());
    }

    @Test
    public void testCreateKebunOverlapped() {
        KoordinatDTO koordinat1 = new KoordinatDTO(50, 50);
        KoordinatDTO koordinat2 = new KoordinatDTO(150, 50);
        KoordinatDTO koordinat3 = new KoordinatDTO(150, 150);
        KoordinatDTO koordinat4 = new KoordinatDTO(50, 150);
        AreaDTO area = new AreaDTO(koordinat1, koordinat2, koordinat3, koordinat4);

        KebunRequestDTO requestDTO = new KebunRequestDTO();
        requestDTO.setNama("Kebun4");
        requestDTO.setLuas(100);
        requestDTO.setArea(area);

        when(kebunRepository.findAll()).thenReturn(kebunList);
        when(overlapChecker.checkOverlap(any(Area.class), any(Area.class))).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kebunService.createKebun(requestDTO);
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
        String id ="aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        UUID uuid = UUID.fromString(id);

        when(kebunRepository.findById(uuid)).thenReturn(Optional.of(kebun1));

        Kebun deletedKebun = kebunService.deleteKebunById(id);
        verify(kebunRepository, times(1)).delete(kebun1);
        assertEquals("Kebun1", deletedKebun.getNama());
    }

    @Test
    public void testDeleteKebunByIdIfNotExist() {
        String id ="aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        UUID uuid = UUID.fromString(id);

        when(kebunRepository.findById(uuid)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kebunService.deleteKebunById(id);
        });
        assertEquals("Kebun with ID " + id + " not found.", exception.getMessage());
    }

    @Test
    public void testDeleteKebunWithMandorAssigned() {
        String id ="aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        UUID uuid = UUID.fromString(id);
        kebun1.setMandorId("mandor123");

        when(kebunRepository.findById(uuid)).thenReturn(Optional.of(kebun1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kebunService.deleteKebunById(id);
        });
        assertEquals("Cannot delete kebun with assigned Mandor. Reassign Mandor first.", exception.getMessage());
        verify(kebunRepository, times(0)).delete(kebun1);
    }

    @Test
    public void testUpdateNamaKebunSuccess() {
        UUID uuid = UUID.fromString("aa558a9a-1a39-460a-8860-71aa6aa63aa6");

        KoordinatDTO koordinat1 = new KoordinatDTO(200, 0);
        KoordinatDTO koordinat2 = new KoordinatDTO(300, 0);
        KoordinatDTO koordinat3 = new KoordinatDTO(300, 200);
        KoordinatDTO koordinat4 = new KoordinatDTO(200, 200);
        AreaDTO area = new AreaDTO(koordinat1, koordinat2, koordinat3, koordinat4);

        KebunRequestDTO kebun = new KebunRequestDTO();
        kebun.setNama("Kebun1 Updated");
        kebun.setLuas(150);
        kebun.setArea(area);

        when(kebunRepository.findById(uuid)).thenReturn(Optional.of(kebun1));
        when(kebunRepository.existsByNama("Kebun1 Updated")).thenReturn(false);
        when(kebunRepository.findAll()).thenReturn(kebunList);
        when(overlapChecker.checkOverlap(any(Area.class), any(Area.class))).thenReturn(false);
        when(kebunRepository.save(any(Kebun.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Kebun result = kebunService.updateKebun(uuid.toString(), kebun);

        assertEquals("Kebun1 Updated", result.getNama());
        assertEquals(150, result.getLuas());
    }

    @Test
    public void testUpdateKebunNameAlreadyExists() {
        UUID uuid = UUID.fromString("aa558a9a-1a39-460a-8860-71aa6aa63aa6");

        KebunRequestDTO updatedData = new KebunRequestDTO();
        updatedData.setNama("Kebun2");

        when(kebunRepository.findById(uuid)).thenReturn(Optional.of(kebun1));
        when(kebunRepository.existsByNama("Kebun2")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kebunService.updateKebun(uuid.toString(), updatedData);
        });

        assertEquals("Kebun with name Kebun2 already exists.", exception.getMessage());
    }

    @Test
    public void testUpdateKebunOverlapped() {
        UUID uuid = UUID.fromString("aa558a9a-1a39-460a-8860-71aa6aa63aa6");

        KoordinatDTO koordinat1 = new KoordinatDTO(50, 0);
        KoordinatDTO koordinat2 = new KoordinatDTO(150, 0);
        KoordinatDTO koordinat3 = new KoordinatDTO(150, 100);
        KoordinatDTO koordinat4 = new KoordinatDTO(50, 100);
        AreaDTO overlappingArea = new AreaDTO(koordinat1, koordinat2, koordinat3, koordinat4);

        KebunRequestDTO updatedData = new KebunRequestDTO();
        updatedData.setNama("Kebun1 Updated");
        updatedData.setLuas(150);
        updatedData.setArea(overlappingArea);

        when(kebunRepository.findById(uuid)).thenReturn(Optional.of(kebun1));
        when(kebunRepository.existsByNama("Kebun1 Updated")).thenReturn(false);
        when(kebunRepository.findAll()).thenReturn(kebunList);

        when(overlapChecker.checkOverlap(any(Area.class), any(Area.class))).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kebunService.updateKebun(uuid.toString(), updatedData);
        });

        assertEquals("Updated kebun overlaps with an existing kebun.", exception.getMessage());
    }

    @Test
    public void testAssignMandorAndPublishSuccess() {
        String kebunId = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        UUID uuid = UUID.fromString(kebunId);
        String mandorId = "mandor123";

        when(kebunRepository.findById(uuid)).thenReturn(Optional.of(kebun1));
        when(kebunRepository.save(any(Kebun.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Kebun updatedKebun = kebunService.assignMandor(kebunId, mandorId);

        assertEquals("mandor123", updatedKebun.getMandorId());
        verify(kebunRepository, times(1)).save(any(Kebun.class));
        verify(applicationEventPublisher, times(1)).publishEvent(any(MandorAssignmentEvent.class));
    }

    @Test
    public void testAssignMandorFromAnotherKebun() {
        String targetKebunId = "bb558b9b-1b39-460b-8860-71bb6bb63bb6";
        UUID targetUuid = UUID.fromString(targetKebunId);
        String mandorId = "mandor123";

        kebun1.setMandorId(mandorId);
        kebun2.setId(targetUuid);
        kebun2.setMandorId(null);

        when(kebunRepository.findById(targetUuid)).thenReturn(Optional.of(kebun2));
        when(kebunRepository.findByMandorId(mandorId)).thenReturn(Optional.of(kebun1));
        when(kebunRepository.save(any(Kebun.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Kebun updatedTargetKebun = kebunService.assignMandor(targetKebunId, mandorId);

        assertNull(kebun1.getMandorId(), "Mandor should be removed from old kebun.");
        assertEquals(mandorId, updatedTargetKebun.getMandorId(), "Mandor should be assigned to new kebun.");

        verify(applicationEventPublisher, times(1)).publishEvent(any(MandorRemovalEvent.class));
        verify(applicationEventPublisher, times(1)).publishEvent(any(MandorAssignmentEvent.class));
    }

    @Test
    public void testAssignMandorToKebunWithOtherMandor() {
        String targetKebunId = "bb558b9b-1b39-460b-8860-71bb6bb63bb6";
        UUID targetUuid = UUID.fromString(targetKebunId);
        String mandorId = "mandor123";

        kebun2.setMandorId("otherMandor");
        kebun2.setId(targetUuid);

        when(kebunRepository.findById(targetUuid)).thenReturn(Optional.of(kebun2));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kebunService.assignMandor(targetKebunId, mandorId);
        });

        assertEquals("Kebun already has a different Mandor assigned. Reassign that Mandor first.", exception.getMessage());
    }

    @Test
    public void testAssignSupirSuccess() {
        String kebunId = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        UUID uuid = UUID.fromString(kebunId);
        String supirId = "supir123";

        kebun1.setSupirIds(new ArrayList<>());

        when(kebunRepository.findBySupirIdsContaining(supirId)).thenReturn(Optional.empty());
        when(kebunRepository.findById(uuid)).thenReturn(Optional.of(kebun1));
        when(kebunRepository.save(any(Kebun.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Kebun updatedKebun = kebunService.assignSupir(kebunId, supirId);

        assertTrue(updatedKebun.getSupirIds().contains(supirId));
        assertEquals(1, updatedKebun.getSupirIds().size());
        verify(kebunRepository, times(1)).save(any(Kebun.class));
        verify(applicationEventPublisher, times(1)).publishEvent(any(SupirAssignmentEvent.class));
    }

    @Test
    public void testAssignSupirAlreadyAssigned() {
        String kebunId = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        UUID uuid = UUID.fromString(kebunId);
        String supirId = "supir123";

        kebun1.setSupirIds(new ArrayList<>(List.of(supirId)));

        when(kebunRepository.findById(uuid)).thenReturn(Optional.of(kebun1));

        Kebun updatedKebun = kebunService.assignSupir(kebunId, supirId);

        assertEquals(1, updatedKebun.getSupirIds().size());
        verify(kebunRepository, times(0)).save(any(Kebun.class));
        verify(applicationEventPublisher, times(0)).publishEvent(any(SupirAssignmentEvent.class));
    }

    @Test
    public void testAssignSupirFromAnotherKebun() {
        String targetKebunId = "bb558b9b-1b39-460b-8860-71bb6bb63bb6";
        UUID targetUuid = UUID.fromString(targetKebunId);
        String supirId = "supir123";

        kebun1.setSupirIds(new ArrayList<>(List.of(supirId)));
        kebun2.setSupirIds(new ArrayList<>());

        when(kebunRepository.findById(targetUuid)).thenReturn(Optional.of(kebun2));
        when(kebunRepository.findBySupirIdsContaining(supirId)).thenReturn(Optional.of(kebun1));
        when(kebunRepository.save(any(Kebun.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Kebun updatedTargetKebun = kebunService.assignSupir(targetKebunId, supirId);

        assertFalse(kebun1.getSupirIds().contains(supirId), "Supir should be removed from old kebun.");
        assertTrue(updatedTargetKebun.getSupirIds().contains(supirId), "Supir should be assigned to new kebun.");

        verify(applicationEventPublisher, times(1)).publishEvent(any(SupirRemovalEvent.class));
        verify(applicationEventPublisher, times(1)).publishEvent(any(SupirAssignmentEvent.class));
    }

    @Test
    public void testRemoveSupirSuccess() {
        String kebunId = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        UUID uuid = UUID.fromString(kebunId);
        String supirId = "supir123";

        kebun1.setSupirIds(new ArrayList<>(List.of(supirId)));

        when(kebunRepository.findById(uuid)).thenReturn(Optional.of(kebun1));
        when(kebunRepository.save(any(Kebun.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Kebun updatedKebun = kebunService.removeSupir(kebunId, supirId);

        assertFalse(updatedKebun.getSupirIds().contains(supirId));
        verify(kebunRepository, times(1)).save(any(Kebun.class));
        verify(applicationEventPublisher, times(1)).publishEvent(any(SupirRemovalEvent.class));
    }

    @Test
    public void testRemoveSupirNotAssigned() {
        String kebunId = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        UUID uuid = UUID.fromString(kebunId);
        String supirId = "supir123";

        kebun1.setSupirIds(new ArrayList<>());

        when(kebunRepository.findById(uuid)).thenReturn(Optional.of(kebun1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kebunService.removeSupir(kebunId, supirId);
        });

        assertEquals("Supir Truk is not assigned to this kebun.", exception.getMessage());
    }

    @Test
    public void testCheckMandorAssignmentisAssigned() {
        String mandorId = "mandor123";
        kebun1.setMandorId(mandorId);

        when(kebunRepository.findByMandorId(mandorId)).thenReturn(Optional.of(kebun1));

        Optional<Kebun> result = kebunService.checkMandorAssignment(mandorId);
        assertTrue(result.isPresent());
        assertEquals("Kebun1", result.get().getNama());
    }

    @Test
    public void testCheckMandorAssignmentNotAssigned() {
        String mandorId = "mandor123";

        when(kebunRepository.findByMandorId(mandorId)).thenReturn(Optional.empty());

        Optional<Kebun> result = kebunService.checkMandorAssignment(mandorId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testCheckSupirAssignmentisAssigned() {
        String supirId = "supir123";
        kebun1.setSupirIds(new ArrayList<>(List.of(supirId)));

        when(kebunRepository.findBySupirIdsContaining(supirId)).thenReturn(Optional.of(kebun1));

        Optional<Kebun> result = kebunService.checkSupirAssignment(supirId);
        assertTrue(result.isPresent());
        assertEquals("Kebun1", result.get().getNama());
    }

    @Test
    public void testCheckSupirAssignmentNotAssigned() {
        String supirId = "supir123";

        when(kebunRepository.findBySupirIdsContaining(supirId)).thenReturn(Optional.empty());

        Optional<Kebun> result = kebunService.checkSupirAssignment(supirId);
        assertFalse(result.isPresent());
    }

}
