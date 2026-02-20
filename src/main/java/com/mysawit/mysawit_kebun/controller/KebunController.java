package com.mysawit.mysawit_kebun.controller;

import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.service.KebunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/kebun")
public class KebunController {

    @Autowired
    private KebunService kebunService;

    @GetMapping
    public ResponseEntity<List<Kebun>> getAllKebun() {
        List<Kebun> kebunList = kebunService.findAllKebun();
        return ResponseEntity.ok(kebunList);
    }

    @PostMapping
    public ResponseEntity<Kebun> createKebun(@RequestBody Kebun kebun) {
        Kebun savedKebun =  kebunService.createKebun(kebun);
        return ResponseEntity.ok(savedKebun);
    }

}
