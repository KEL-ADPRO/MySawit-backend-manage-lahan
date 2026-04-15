package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.DTO.KebunRequestDTO;
import com.mysawit.mysawit_kebun.model.Kebun;

import java.util.List;

public interface KebunService {
    List<Kebun> findAllKebun();
    Kebun findById(String id);
    Kebun findByName(String name);
    Kebun createKebun(KebunRequestDTO requestDTO);
    Kebun deleteKebunById(String id);
    Kebun updateKebun(String id, KebunRequestDTO requestDTO);
    Kebun assignMandor(String kebunId, String mandorId);
}
