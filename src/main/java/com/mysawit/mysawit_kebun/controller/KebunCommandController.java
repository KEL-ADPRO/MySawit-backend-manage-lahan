package com.mysawit.mysawit_kebun.controller;

import com.mysawit.mysawit_kebun.dto.KebunRequestDto;
import com.mysawit.mysawit_kebun.dto.WebResponse;
import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.service.KebunService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/kebun")
@RequiredArgsConstructor
public class KebunCommandController {
    private final KebunService kebunService;

    @PostMapping
    public ResponseEntity<WebResponse<?>> createKebun(@Valid @RequestBody KebunRequestDto requestDTO) {
        Kebun savedKebun = kebunService.createKebun(requestDTO);
        return ResponseEntity.ok(WebResponse.builder()
                .message("Kebun created successfully")
                .data(savedKebun)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WebResponse<?>> updateKebun(@PathVariable String id, @Valid @RequestBody KebunRequestDto updatedData) {
        Kebun updatedKebun = kebunService.updateKebun(id, updatedData);
        return ResponseEntity.ok(WebResponse.builder()
                .message("Kebun updated successfully")
                .data(updatedKebun)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<?>> deleteKebunById(@PathVariable String id) {
        kebunService.deleteKebunById(id);
        return ResponseEntity.ok(WebResponse.<Map<String, String>>builder()
                .message("Kebun deleted successfully")
                .data(Map.of("deletedId", id))
                .build());
    }
}
