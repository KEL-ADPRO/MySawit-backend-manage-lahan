package com.mysawit.mysawit_kebun.controller;

import com.mysawit.mysawit_kebun.dto.KebunRequestDto;
import com.mysawit.mysawit_kebun.exception.KebunDuplicateNameException;
import com.mysawit.mysawit_kebun.exception.KebunNotFoundException;
import com.mysawit.mysawit_kebun.exception.KebunOverlapException;
import com.mysawit.mysawit_kebun.model.Kebun;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KebunCommandController.class)
class KebunCommandControllerTest extends KebunControllerTestBase {

    @Test
    @WithMockUser(authorities = adminAuth)
    void testCreateKebun() throws Exception {
        when(kebunService.createKebun(any(KebunRequestDto.class))).thenReturn(kebun1);

        mockMvc.perform(post("/api/kebun")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kebun1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nama").value("Kebun1"))
                .andExpect(jsonPath("$.message").value("Kebun created successfully"));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    void testCreateKebunOverlap() throws Exception {
        when(kebunService.createKebun(any(KebunRequestDto.class)))
                .thenThrow(new KebunOverlapException("Kebun overlaps with an existing kebun."));

        mockMvc.perform(post("/api/kebun")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kebun1)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Kebun overlaps with an existing kebun."));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    void testDeleteKebun() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";

        when(kebunService.deleteKebunById(id)).thenReturn(kebun1);

        mockMvc.perform(delete("/api/kebun/" + id).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Kebun deleted successfully"))
                .andExpect(jsonPath("$.data.deletedId").value(id));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    void testDeleteKebunNotFound() throws Exception {
        String badId = "dd558d9d-1d39-460d-8860-71dd6dd63dd6";

        when(kebunService.deleteKebunById(badId)).thenThrow(new KebunNotFoundException("Kebun with ID " + badId + " not found."));

        mockMvc.perform(delete("/api/kebun/" + badId).with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Kebun with ID " + badId + " not found."));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    void testUpdateKebunSuccess() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";

        KebunRequestDto updateRequest = new KebunRequestDto();
        updateRequest.setNama("Kebun1 Updated");
        updateRequest.setLuas(150);

        Kebun updatedData = new Kebun();
        updatedData.setId(UUID.fromString(id));
        updatedData.setNama("Kebun1 Updated");
        updatedData.setLuas(150);
        updatedData.setArea(kebun1.getArea());

        when(kebunService.updateKebun(eq(id), any(KebunRequestDto.class))).thenReturn(updatedData);

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
    void testUpdateKebunSameName() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";

        KebunRequestDto requestDTO = new KebunRequestDto();
        requestDTO.setNama("Kebun2");

        when(kebunService.updateKebun(eq(id), any(KebunRequestDto.class)))
                .thenThrow(new KebunDuplicateNameException("Kebun with name Kebun2 already exists."));

        mockMvc.perform(put("/api/kebun/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kebun1)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Kebun with name Kebun2 already exists."));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    void testUpdateKebunOverlap() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";

        KebunRequestDto requestDTO = new KebunRequestDto();
        requestDTO.setNama("Kebun1 Updated");

        when(kebunService.updateKebun(eq(id), any(KebunRequestDto.class)))
                .thenThrow(new KebunOverlapException("Updated kebun overlaps with an existing kebun."));

        mockMvc.perform(put("/api/kebun/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kebun1)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Updated kebun overlaps with an existing kebun."));
    }
}

