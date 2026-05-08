package com.mysawit.mysawit_kebun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@Import(SecurityConfig.class)
abstract class KebunControllerTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected KebunService kebunService;

    @MockitoBean
    protected JWTFilter jwtFilter;

    @Autowired
    protected ObjectMapper objectMapper;

    protected final String adminAuth = "ADMIN";

    protected Kebun kebun1;
    protected Kebun kebun2;

    @BeforeEach
    void setup() throws Exception {
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
}

