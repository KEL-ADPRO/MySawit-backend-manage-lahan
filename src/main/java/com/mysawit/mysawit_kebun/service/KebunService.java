package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.model.Kebun;

import java.util.List;

public interface KebunService {
    List<Kebun> findAllKebun();
    Kebun findById(String id);
    Kebun findByName(String name);
    Kebun createKebun(Kebun kebun);
    Kebun deleteKebunById(Kebun kebun);
}
