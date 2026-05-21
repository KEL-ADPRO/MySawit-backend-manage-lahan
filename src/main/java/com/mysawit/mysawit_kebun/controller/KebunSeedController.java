package com.mysawit.mysawit_kebun.controller;

import com.mysawit.mysawit_kebun.dto.AreaDto;
import com.mysawit.mysawit_kebun.dto.KebunRequestDto;
import com.mysawit.mysawit_kebun.dto.KoordinatDto;
import com.mysawit.mysawit_kebun.dto.WebResponse;
import com.mysawit.mysawit_kebun.service.KebunService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/kebun/seed")
@RequiredArgsConstructor
public class KebunSeedController {

    private final KebunService kebunService;

    @PostMapping
    public ResponseEntity<WebResponse<?>> seedKebun(@RequestParam(defaultValue = "100") int count) {
        log.info("Starting performance data seeding for {} lands...", count);
        int successCount = 0;
        int failureCount = 0;

        for (int i = 0; i < count; i++) {
            // Calculate non-overlapping coordinates in sequence
            int offset = i * 15;
            KoordinatDto bottomLeft = new KoordinatDto(offset, 0);
            KoordinatDto bottomRight = new KoordinatDto(offset + 10, 0);
            KoordinatDto topRight = new KoordinatDto(offset + 10, 10);
            KoordinatDto topLeft = new KoordinatDto(offset, 10);

            AreaDto areaDto = new AreaDto(bottomLeft, bottomRight, topRight, topLeft);
            String uniqueName = "Seeded Kebun " + i + "-" + System.nanoTime();
            KebunRequestDto requestDto = new KebunRequestDto(uniqueName, 100.0, areaDto);

            try {
                kebunService.createKebun(requestDto);
                successCount++;
            } catch (Exception ex) {
                failureCount++;
                log.warn("Seeding failed for index {}: {}", i, ex.getMessage());
            }
        }

        log.info("Data seeding finished. Successfully seeded: {}, Failed: {}", successCount, failureCount);

        return ResponseEntity.ok(WebResponse.<Map<String, Integer>>builder()
                .message("Seeding completed successfully")
                .data(Map.of(
                        "successCount", successCount,
                        "failureCount", failureCount
                ))
                .build());
    }
}
