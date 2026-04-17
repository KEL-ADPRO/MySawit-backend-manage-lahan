package com.mysawit.mysawit_kebun.controller;

import com.mysawit.mysawit_kebun.dto.KebunRequestDto;
import com.mysawit.mysawit_kebun.dto.WebResponse;
import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.service.KebunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/kebun")
@RequiredArgsConstructor
public class KebunController {

    private final KebunService kebunService;

    @GetMapping
    public ResponseEntity<WebResponse<List<Kebun>>> getAllKebun() {
        List<Kebun> kebunList = kebunService.findAllKebun();
        return ResponseEntity.ok(WebResponse.<List<Kebun>>builder()
                .message("Success")
                .data(kebunList)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebResponse<?>> getKebunById(@PathVariable String id) {
        try {
            Kebun kebun = kebunService.findById(id);
            return ResponseEntity.ok(WebResponse.builder()
                    .message("Success")
                    .data(kebun)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(WebResponse.builder().message(e.getMessage()).build());
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<WebResponse<?>> getKebunByName(@PathVariable String name) {
        try {
            Kebun kebun = kebunService.findByName(name);
            return ResponseEntity.ok(WebResponse.builder()
                    .message("Success")
                    .data(kebun)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(WebResponse.builder().message(e.getMessage()).build());
        }
    }

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

    @GetMapping("/check-mandor/{mandorId}")
    public ResponseEntity<WebResponse<?>> checkMandorAssignment(@PathVariable String mandorId) {
        try {
            var kebunAssignment = kebunService.checkMandorAssignment(mandorId);
            if (kebunAssignment.isPresent()) {
                Kebun kebun = kebunAssignment.get();
                return ResponseEntity.ok(WebResponse.builder()
                        .message("Mandor assignment found")
                        .data(Map.of(
                                "isAssigned", true,
                                "kebunId", kebun.getId().toString(),
                                "namaKebun", kebun.getNama()
                        ))
                        .build());
            }

            return ResponseEntity.ok(WebResponse.builder()
                    .message("Mandor is not assigned to any kebun")
                    .data(Map.of("isAssigned", false))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(WebResponse.builder().message(e.getMessage()).build());
        }
    }

    @GetMapping("/check-supir/{supirId}")
    public ResponseEntity<WebResponse<?>> checkSupirAssignment(@PathVariable String supirId) {
        try {
            var kebunAssignment = kebunService.checkSupirAssignment(supirId);
            if (kebunAssignment.isPresent()) {
                Kebun kebun = kebunAssignment.get();
                return ResponseEntity.ok(WebResponse.builder()
                        .message("Supir assignment found")
                        .data(Map.of(
                                "isAssigned", true,
                                "kebunId", kebun.getId().toString(),
                                "namaKebun", kebun.getNama()
                        ))
                        .build());
            }

            return ResponseEntity.ok(WebResponse.builder()
                    .message("Supir Truk is not assigned to any kebun")
                    .data(Map.of("isAssigned", false))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(WebResponse.builder().message(e.getMessage()).build());
        }
    }
}
