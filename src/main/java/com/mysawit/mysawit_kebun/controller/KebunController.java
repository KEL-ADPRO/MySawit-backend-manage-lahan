package com.mysawit.mysawit_kebun.controller;

import com.mysawit.mysawit_kebun.DTO.KebunRequestDTO;
import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.service.KebunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/kebun")
@RequiredArgsConstructor
public class KebunController {

    private final KebunService kebunService;

    @GetMapping
    public ResponseEntity<List<Kebun>> getAllKebun() {
        List<Kebun> kebunList = kebunService.findAllKebun();
        return ResponseEntity.ok(kebunList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKebunById(@PathVariable String id) {
        try {
            Kebun kebun = kebunService.findById(id);
            return ResponseEntity.ok(kebun);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getKebunByName(@PathVariable String name) {
        try {
            Kebun kebun = kebunService.findByName(name);
            return ResponseEntity.ok(kebun);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createKebun(@RequestBody KebunRequestDTO requestDTO) {
        try {
            Kebun savedKebun = kebunService.createKebun(requestDTO);
            return ResponseEntity.ok(savedKebun);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKebunById(@PathVariable String id) {
        try {
            Kebun kebun = kebunService.findById(id);
            Kebun deletedKebun = kebunService.deleteKebunById(id);
            return ResponseEntity.ok(Map.of("message", "Kebun deleted successfully", "data", deletedKebun));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateKebun(@PathVariable String id, @RequestBody KebunRequestDTO updatedData) {
        try {
            Kebun updatedKebun = kebunService.updateKebun(id, updatedData);
            return ResponseEntity.ok(Map.of("message", "Kebun updated successfully", "data", updatedKebun));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{kebunId}/mandor/{mandorId}")
    public ResponseEntity<?> assignMandor(@PathVariable String kebunId, @PathVariable String mandorId) {
        try {
            Kebun updatedKebun = kebunService.assignMandor(kebunId, mandorId);
            return ResponseEntity.ok(Map.of("message", "Mandor assigned successfully", "data", updatedKebun));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{kebunId}/supir/{supirId}")
    public ResponseEntity<?> assignSupir(@PathVariable String kebunId, @PathVariable String supirId) {
        try {
            Kebun updatedKebun = kebunService.assignSupir(kebunId, supirId);
            return ResponseEntity.ok(Map.of("message", "Supir Truk assigned successfully", "data", updatedKebun));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{kebunId}/supir/{supirId}")
    public ResponseEntity<?> removeSupir(@PathVariable String kebunId, @PathVariable String supirId) {
        try {
            Kebun updatedKebun = kebunService.removeSupir(kebunId, supirId);
            return ResponseEntity.ok(Map.of("message", "Supir Truk removed successfully", "data", updatedKebun));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
