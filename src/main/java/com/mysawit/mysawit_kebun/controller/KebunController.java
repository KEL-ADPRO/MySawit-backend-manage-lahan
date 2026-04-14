package com.mysawit.mysawit_kebun.controller;

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
    public ResponseEntity<?> createKebun(@RequestBody Kebun kebun) {
        try {
            Kebun savedKebun = kebunService.createKebun(kebun);
            return ResponseEntity.ok(savedKebun);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteKebun(@PathVariable Kebun kebun) {
        try {
            Kebun deletedKebun = kebunService.deleteKebunById(kebun);
            return ResponseEntity.ok(Map.of("message", "Kebun deleted successfully", "data", deletedKebun));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKebunById(@PathVariable String id) {
        try {
            Kebun kebun = kebunService.findById(id);
            Kebun deletedKebun = kebunService.deleteKebunById(kebun);
            return ResponseEntity.ok(Map.of("message", "Kebun deleted successfully", "data", deletedKebun));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateKebun(@PathVariable String id, @RequestBody Kebun updatedData) {
        try {
            Kebun updatedKebun = kebunService.updateKebun(id, updatedData);
            return ResponseEntity.ok(Map.of("message", "Kebun updated successfully", "data", updatedKebun));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
