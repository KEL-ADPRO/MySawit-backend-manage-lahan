package com.mysawit.mysawit_kebun.controller;

import com.mysawit.mysawit_kebun.dto.WebResponse;
import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.service.KebunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/kebun")
@RequiredArgsConstructor
public class KebunQueryController {
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
