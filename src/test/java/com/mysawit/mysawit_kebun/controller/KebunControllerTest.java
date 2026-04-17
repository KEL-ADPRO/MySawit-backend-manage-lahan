package com.mysawit.mysawit_kebun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysawit.mysawit_kebun.DTO.KebunRequestDTO;
import com.mysawit.mysawit_kebun.model.Area;
import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.model.Koordinat;
import com.mysawit.mysawit_kebun.security.JWTFilter;
import com.mysawit.mysawit_kebun.security.SecurityConfig;
import com.mysawit.mysawit_kebun.service.KebunService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KebunController.class)
@Import(SecurityConfig.class)
public class KebunControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KebunService kebunService;

    @MockitoBean
    private JWTFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private final String adminAuth = "ADMIN";

    private Kebun kebun1;
    private Kebun kebun2;


    @BeforeEach
    public void setup() throws Exception {
        doAnswer(invocation -> {
            ServletRequest request = invocation.getArgument(0);
            ServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);

            chain.doFilter(request, response);
            return null;
        }).when(jwtFilter).doFilter(any(), any(), any());

        Area area1 = new Area(
                new Koordinat(0, 0),
                new Koordinat(100, 0),
                new Koordinat(100, 100),
                new Koordinat(0, 100)
        );
        kebun1 = new Kebun();
        kebun1.setId(UUID.fromString("aa558a9a-1a39-460a-8860-71aa6aa63aa6"));
        kebun1.setNama("Kebun1");
        kebun1.setLuas(100);
        kebun1.setArea(area1);

        Area area2 = new Area(
                new Koordinat(100, 0),
                new Koordinat(200, 0),
                new Koordinat(200, 100),
                new Koordinat(100, 100)
        );
        kebun2 = new Kebun();
        kebun2.setId(UUID.fromString("bb558b9b-1b39-460b-8860-71bb6bb63bb6"));
        kebun2.setNama("Kebun2");
        kebun2.setLuas(100);
        kebun2.setArea(area2);
    }

    @Test
    public void testGetAllKebun() throws Exception {
        when(kebunService.findAllKebun()).thenReturn(List.of(kebun1, kebun2));

        mockMvc.perform(get("/api/kebun"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nama").value("Kebun1"));
    }

    @Test
    public void testGetKebunById() throws Exception {
        when(kebunService.findById("aa558a9a-1a39-460a-8860-71aa6aa63aa6")).thenReturn(kebun1);

        mockMvc.perform(get("/api/kebun/aa558a9a-1a39-460a-8860-71aa6aa63aa6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nama").value("Kebun1"));
    }

    @Test
    public void testGetKebunByIdNotFound() throws Exception {
        String badId = "dd558d9d-1d39-460d-8860-71dd6dd63dd6";
        when(kebunService.findById(badId)).thenThrow(new IllegalArgumentException("Kebun with ID " + badId + " not found."));

        mockMvc.perform(get("/api/kebun/" + badId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Kebun with ID " + badId + " not found."));
    }

    @Test
    public void testGetKebunByName() throws Exception {
        when(kebunService.findByName("Kebun2")).thenReturn(kebun2);

        mockMvc.perform(get("/api/kebun/name/Kebun2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("bb558b9b-1b39-460b-8860-71bb6bb63bb6"));
    }

    @Test
    public void testGetKebunByNameNotFound() throws Exception {
        when(kebunService.findByName("Kebun4")).thenThrow(new IllegalArgumentException("Kebun with name Kebun4 not found."));

        mockMvc.perform(get("/api/kebun/name/Kebun4"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Kebun with name Kebun4 not found."));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    public void testCreateKebun() throws Exception {
        when(kebunService.createKebun(any(KebunRequestDTO.class))).thenReturn(kebun1);

        mockMvc.perform(post("/api/kebun")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kebun1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nama").value("Kebun1"));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    public void testCreateKebunOverlap() throws Exception {
        when(kebunService.createKebun(any(KebunRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Kebun overlaps with an existing kebun."));

        mockMvc.perform(post("/api/kebun")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kebun1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Kebun overlaps with an existing kebun."));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    public void testDeleteKebun() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";

        when(kebunService.deleteKebunById(id)).thenReturn(kebun1);

        mockMvc.perform(delete("/api/kebun/" + id).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Kebun deleted successfully"))
                .andExpect(jsonPath("$.deletedId").value(id));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    public void testDeleteKebunNotFound() throws Exception {
        String badId = "dd558d9d-1d39-460d-8860-71dd6dd63dd6";

        when(kebunService.deleteKebunById(badId)).thenThrow(new IllegalArgumentException("Kebun with ID " + badId + " not found."));

        mockMvc.perform(delete("/api/kebun/" + badId).with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Kebun with ID " + badId + " not found."));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    public void testUpdateKebunSuccess() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";

        KebunRequestDTO updateRequest = new KebunRequestDTO();
        updateRequest.setNama("Kebun1 Updated");
        updateRequest.setLuas(150);

        Kebun updatedData = new Kebun();
        updatedData.setId(UUID.fromString(id));
        updatedData.setNama("Kebun1 Updated");
        updatedData.setLuas(150);
        updatedData.setArea(kebun1.getArea());

        when(kebunService.updateKebun(eq(id), any(KebunRequestDTO.class))).thenReturn(updatedData);

        mockMvc.perform(put("/api/kebun/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Kebun updated successfully"))
                .andExpect(jsonPath("$.data.nama").value("Kebun1 Updated"));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    public void testUpdateKebunSameName() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";

        KebunRequestDTO requestDTO = new KebunRequestDTO();
        requestDTO.setNama("Kebun2");

        when(kebunService.updateKebun(eq(id), any(KebunRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Kebun with name Kebun2 already exists."));

        mockMvc.perform(put("/api/kebun/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kebun1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Kebun with name Kebun2 already exists."));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    public void testUpdateKebunOverlap() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";

        KebunRequestDTO requestDTO = new KebunRequestDTO();
        requestDTO.setNama("Kebun1 Updated");

        when(kebunService.updateKebun(eq(id), any(KebunRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Updated kebun overlaps with an existing kebun."));

        mockMvc.perform(put("/api/kebun/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kebun1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Updated kebun overlaps with an existing kebun."));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    public void testAssignMandorSucces() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        String mandorId = "mandor123";

        Kebun updatedKebun = new Kebun();
        updatedKebun.setId(UUID.fromString(id));
        updatedKebun.setNama("Kebun1");
        updatedKebun.setLuas(100);
        updatedKebun.setArea(kebun1.getArea());
        updatedKebun.setMandorId(mandorId);

        when(kebunService.assignMandor(id, mandorId)).thenReturn(updatedKebun);

        mockMvc.perform(patch("/api/kebun/" + id + "/mandor/" + mandorId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Mandor assigned successfully"))
                .andExpect(jsonPath("$.data.mandorId").value(mandorId));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    public void testAssignmandorKebunNotFound() throws Exception {
        String badId = "dd558d9d-1d39-460d-8860-71dd6dd63dd6";
        String mandorId = "mandor123";

        when(kebunService.assignMandor(badId, mandorId)).thenThrow(new IllegalArgumentException("Kebun with ID " + badId + " not found."));

        mockMvc.perform(patch("/api/kebun/" + badId + "/mandor/" + mandorId).with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Kebun with ID " + badId + " not found."));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    public void testAssignSupirSuccess() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        String supirId = "supir123";

        Kebun updatedKebun = new Kebun();
        updatedKebun.setId(UUID.fromString(id));
        updatedKebun.setNama("Kebun1");
        updatedKebun.setLuas(100);
        updatedKebun.setArea(kebun1.getArea());
        updatedKebun.setSupirIds(List.of(supirId));

        when(kebunService.assignSupir(id, supirId)).thenReturn(updatedKebun);

        mockMvc.perform(patch("/api/kebun/" + id + "/supir/" + supirId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Supir Truk assigned successfully"))
                .andExpect(jsonPath("$.data.supirIds[0]").value(supirId));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    public void testAssignSupirAlreadyAssigned() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        String supirId = "supir123";

        Kebun updatedKebun = new Kebun();
        updatedKebun.setId(UUID.fromString(id));
        updatedKebun.setNama("Kebun1");
        updatedKebun.setSupirIds(List.of(supirId));

        when(kebunService.assignSupir(id, supirId)).thenReturn(updatedKebun);

        mockMvc.perform(patch("/api/kebun/" + id + "/supir/" + supirId).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    public void testRemoveSupirSuccess() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        String supirId = "supir123";

        Kebun updatedKebun = new Kebun();
        updatedKebun.setId(UUID.fromString(id));
        updatedKebun.setNama("Kebun1");
        updatedKebun.setLuas(100);
        updatedKebun.setArea(kebun1.getArea());
        updatedKebun.setSupirIds(new ArrayList<>());

        when(kebunService.removeSupir(id, supirId)).thenReturn(updatedKebun);

        mockMvc.perform(delete("/api/kebun/" + id + "/supir/" + supirId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Supir Truk removed successfully"))
                .andExpect(jsonPath("$.data.supirIds.length()").value(0));
    }

     @Test
     @WithMockUser(authorities = adminAuth)
     public void testRemoveSupirNotAssigned() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        String supirId = "supir123";

        when(kebunService.removeSupir(id, supirId)).thenThrow(new IllegalArgumentException("Supir Truk is not assigned to this kebun."));

        mockMvc.perform(delete("/api/kebun/" + id + "/supir/" + supirId).with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Supir Truk is not assigned to this kebun."));
    }

    @Test
    public void testCheckMandorAssignmentIsAssigned() throws Exception {
        String mandorId = "mandor123";

        when(kebunService.checkMandorAssignment(mandorId)).thenReturn(java.util.Optional.of(kebun1));

        mockMvc.perform(get("/api/kebun/check-mandor/" + mandorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAssigned").value(true));
    }

    @Test
    public void testCheckMandorAssignmentNotAssigned() throws Exception {
        String mandorId = "mandor123";

        when(kebunService.checkMandorAssignment(mandorId)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/kebun/check-mandor/" + mandorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAssigned").value(false));
    }

    @Test
    public void testCheckMandorASsignmentMandorNotexist() throws Exception {
        String mandorId = "mandor123";

        when(kebunService.checkMandorAssignment(mandorId)).thenThrow(new IllegalArgumentException("Mandor with ID " + mandorId + " not found."));

        mockMvc.perform(get("/api/kebun/check-mandor/" + mandorId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Mandor with ID " + mandorId + " not found."));
    }

    @Test
    public void testCheckSupirAssignmentIsAssigned() throws Exception {
        String supirId = "supir123";

        when(kebunService.checkSupirAssignment(supirId)).thenReturn(java.util.Optional.of(kebun1));

        mockMvc.perform(get("/api/kebun/check-supir/" + supirId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAssigned").value(true));
    }

    @Test
    public void testCheckSupirAssignmentNotAssigned() throws Exception {
        String supirId = "supir123";

        when(kebunService.checkSupirAssignment(supirId)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/kebun/check-supir/" + supirId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAssigned").value(false));
    }

    @Test
    public void testCheckSupirAssignmentSupirNotExist() throws Exception {
        String supirId = "supir123";

        when(kebunService.checkSupirAssignment(supirId)).thenThrow(new IllegalArgumentException("Supir with ID " + supirId + " not found."));

        mockMvc.perform(get("/api/kebun/check-supir/" + supirId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Supir with ID " + supirId + " not found."));
    }
}
