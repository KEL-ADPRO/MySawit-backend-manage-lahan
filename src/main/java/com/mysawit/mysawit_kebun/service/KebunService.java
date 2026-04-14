package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.DTO.KebunRequestDTO;
import com.mysawit.mysawit_kebun.model.Kebun;

import java.util.List;

public interface KebunService {
    List<Kebun> findAllKebun();
    Kebun findById(String id);
    Kebun findByName(String name);
    Kebun createKebun(KebunRequestDTO requestDTO);
    Kebun deleteKebunById(Kebun kebun);
    Kebun updateKebun(String id, KebunRequestDTO requestDTO);
}
