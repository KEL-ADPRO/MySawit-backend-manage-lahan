package com.mysawit.mysawit_kebun.controller;

import com.mysawit.mysawit_kebun.dto.WebResponse;
import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.service.KebunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/kebun")
@RequiredArgsConstructor
public class KebunAssignmentController {
    private final KebunService kebunService;

    @PatchMapping("/{kebunId}/mandor/{mandorId}")
    public ResponseEntity<WebResponse<?>> assignMandor(@PathVariable String kebunId, @PathVariable String mandorId) {
        try {
            Kebun updatedKebun = kebunService.assignMandor(kebunId, mandorId);
            return ResponseEntity.ok(WebResponse.builder()
                    .message("Mandor assigned successfully")
                    .data(updatedKebun)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(WebResponse.builder().message(e.getMessage()).build());
        }
    }

    @PatchMapping("/{kebunId}/supir/{supirId}")
    public ResponseEntity<WebResponse<?>> assignSupir(@PathVariable String kebunId, @PathVariable String supirId) {
        try {
            Kebun updatedKebun = kebunService.assignSupir(kebunId, supirId);
            return ResponseEntity.ok(WebResponse.builder()
                    .message("Supir Truk assigned successfully")
                    .data(updatedKebun)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(WebResponse.builder().message(e.getMessage()).build());
        }
    }

    @DeleteMapping("/{kebunId}/supir/{supirId}")
    public ResponseEntity<WebResponse<?>> removeSupir(@PathVariable String kebunId, @PathVariable String supirId) {
        try {
            Kebun updatedKebun = kebunService.removeSupir(kebunId, supirId);
            return ResponseEntity.ok(WebResponse.builder()
                    .message("Supir Truk removed successfully")
                    .data(updatedKebun)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(WebResponse.builder().message(e.getMessage()).build());
        }
    }
}
