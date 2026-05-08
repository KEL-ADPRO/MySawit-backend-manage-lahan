package com.mysawit.mysawit_kebun.controller;

import com.mysawit.mysawit_kebun.dto.KebunRequestDto;
import com.mysawit.mysawit_kebun.dto.WebResponse;
import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.service.KebunService;
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
    public ResponseEntity<WebResponse<?>> createKebun(@RequestBody KebunRequestDto requestDTO) {
        try {
            Kebun savedKebun = kebunService.createKebun(requestDTO);
            return ResponseEntity.ok(WebResponse.builder()
                    .message("Kebun created successfully")
                    .data(savedKebun)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(WebResponse.builder().message(e.getMessage()).build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<WebResponse<?>> updateKebun(@PathVariable String id, @RequestBody KebunRequestDto updatedData) {
        try {
            Kebun updatedKebun = kebunService.updateKebun(id, updatedData);
            return ResponseEntity.ok(WebResponse.builder()
                    .message("Kebun updated successfully")
                    .data(updatedKebun)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(WebResponse.builder().message(e.getMessage()).build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<?>> deleteKebunById(@PathVariable String id) {
        try {
            kebunService.deleteKebunById(id);
            return ResponseEntity.ok(WebResponse.<Map<String, String>>builder()
                    .message("Kebun deleted successfully")
                    .data(Map.of("deletedId", id))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(WebResponse.builder().message(e.getMessage()).build());
        }
    }
}
