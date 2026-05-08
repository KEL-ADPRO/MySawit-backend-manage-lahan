package com.mysawit.mysawit_kebun.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KebunQueryController.class)
class KebunQueryControllerTest extends KebunControllerTestBase {

    @Test
    void testGetAllKebun() throws Exception {
        when(kebunService.findAllKebun()).thenReturn(java.util.List.of(kebun1, kebun2));

        mockMvc.perform(get("/api/kebun"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].nama").value("Kebun1"))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testGetKebunById() throws Exception {
        when(kebunService.findById("aa558a9a-1a39-460a-8860-71aa6aa63aa6")).thenReturn(kebun1);

        mockMvc.perform(get("/api/kebun/aa558a9a-1a39-460a-8860-71aa6aa63aa6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nama").value("Kebun1"))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testGetKebunByIdNotFound() throws Exception {
        String badId = "dd558d9d-1d39-460d-8860-71dd6dd63dd6";
        when(kebunService.findById(badId)).thenThrow(new IllegalArgumentException("Kebun with ID " + badId + " not found."));

        mockMvc.perform(get("/api/kebun/" + badId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Kebun with ID " + badId + " not found."));
    }

    @Test
    void testGetKebunByName() throws Exception {
        when(kebunService.findByName("Kebun2")).thenReturn(kebun2);

        mockMvc.perform(get("/api/kebun/name/Kebun2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("bb558b9b-1b39-460b-8860-71bb6bb63bb6"))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testGetKebunByNameNotFound() throws Exception {
        when(kebunService.findByName("Kebun4")).thenThrow(new IllegalArgumentException("Kebun with name Kebun4 not found."));

        mockMvc.perform(get("/api/kebun/name/Kebun4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Kebun with name Kebun4 not found."));
    }

    @Test
    void testCheckMandorAssignmentIsAssigned() throws Exception {
        String mandorId = "mandor123";

        when(kebunService.checkMandorAssignment(mandorId)).thenReturn(java.util.Optional.of(kebun1));

        mockMvc.perform(get("/api/kebun/check-mandor/" + mandorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isAssigned").value(true))
                .andExpect(jsonPath("$.message").value("Mandor assignment found"));
    }

    @Test
    void testCheckMandorAssignmentNotAssigned() throws Exception {
        String mandorId = "mandor123";

        when(kebunService.checkMandorAssignment(mandorId)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/kebun/check-mandor/" + mandorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isAssigned").value(false))
                .andExpect(jsonPath("$.message").value("Mandor is not assigned to any kebun"));
    }

    @Test
    void testCheckMandorAssignmentMandorNotExist() throws Exception {
        String mandorId = "mandor123";

        when(kebunService.checkMandorAssignment(mandorId)).thenThrow(new IllegalArgumentException("Mandor with ID " + mandorId + " not found."));

        mockMvc.perform(get("/api/kebun/check-mandor/" + mandorId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Mandor with ID " + mandorId + " not found."));
    }

    @Test
    void testCheckSupirAssignmentIsAssigned() throws Exception {
        String supirId = "supir123";

        when(kebunService.checkSupirAssignment(supirId)).thenReturn(java.util.Optional.of(kebun1));

        mockMvc.perform(get("/api/kebun/check-supir/" + supirId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isAssigned").value(true))
                .andExpect(jsonPath("$.message").value("Supir assignment found"));
    }

    @Test
    void testCheckSupirAssignmentNotAssigned() throws Exception {
        String supirId = "supir123";

        when(kebunService.checkSupirAssignment(supirId)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/kebun/check-supir/" + supirId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isAssigned").value(false))
                .andExpect(jsonPath("$.message").value("Supir Truk is not assigned to any kebun"));
    }

    @Test
    void testCheckSupirAssignmentSupirNotExist() throws Exception {
        String supirId = "supir123";

        when(kebunService.checkSupirAssignment(supirId)).thenThrow(new IllegalArgumentException("Supir with ID " + supirId + " not found."));

        mockMvc.perform(get("/api/kebun/check-supir/" + supirId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Supir with ID " + supirId + " not found."));
    }
}

