package com.mysawit.mysawit_kebun.controller;

import com.mysawit.mysawit_kebun.exception.KebunInvalidOperationException;
import com.mysawit.mysawit_kebun.exception.KebunNotFoundException;
import com.mysawit.mysawit_kebun.model.Kebun;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KebunAssignmentController.class)
class KebunAssignmentControllerTest extends KebunControllerTestBase {

    @Test
    @WithMockUser(authorities = adminAuth)
    void testAssignMandorSuccess() throws Exception {
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
    void testAssignMandorKebunNotFound() throws Exception {
        String badId = "dd558d9d-1d39-460d-8860-71dd6dd63dd6";
        String mandorId = "mandor123";

        when(kebunService.assignMandor(badId, mandorId))
                .thenThrow(new KebunNotFoundException("Kebun with ID " + badId + " not found."));

        mockMvc.perform(patch("/api/kebun/" + badId + "/mandor/" + mandorId).with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Kebun with ID " + badId + " not found."));
    }

    @Test
    @WithMockUser(authorities = adminAuth)
    void testAssignSupirSuccess() throws Exception {
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
    void testAssignSupirAlreadyAssigned() throws Exception {
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
    void testRemoveSupirSuccess() throws Exception {
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
    void testRemoveSupirNotAssigned() throws Exception {
        String id = "aa558a9a-1a39-460a-8860-71aa6aa63aa6";
        String supirId = "supir123";

        when(kebunService.removeSupir(id, supirId))
                .thenThrow(new KebunInvalidOperationException("Supir Truk is not assigned to this kebun."));

        mockMvc.perform(delete("/api/kebun/" + id + "/supir/" + supirId).with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Supir Truk is not assigned to this kebun."));
    }
}

