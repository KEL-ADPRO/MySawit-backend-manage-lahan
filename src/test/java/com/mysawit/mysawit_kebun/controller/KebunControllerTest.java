package com.mysawit.mysawit_kebun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysawit.mysawit_kebun.model.Area;
import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.model.Koordinat;
import com.mysawit.mysawit_kebun.service.KebunService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KebunController.class)
public class KebunControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KebunService kebunService;

    @Autowired
    private ObjectMapper objectMapper;

    private Kebun kebun1;
    private Kebun kebun2;

    @BeforeEach
    public void setup() {
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
    public void testCreateKebun() throws Exception {
        when(kebunService.createKebun(any(Kebun.class))).thenReturn(kebun1);

        mockMvc.perform(post("/api/kebun")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kebun1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nama").value("Kebun1"));
    }

    @Test
    public void testCreateKebunOverlap() throws Exception {
        when(kebunService.createKebun(any(Kebun.class))).thenReturn(null);

        mockMvc.perform(post("/api/kebun")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kebun1)))
                .andExpect(status().isBadRequest());
    }
}
