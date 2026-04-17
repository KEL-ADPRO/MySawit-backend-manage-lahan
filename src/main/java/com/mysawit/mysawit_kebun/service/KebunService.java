package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.dto.KebunRequestDto;
import com.mysawit.mysawit_kebun.model.Kebun;

import java.util.List;
import java.util.Optional;

public interface KebunService {
    List<Kebun> findAllKebun();
    Kebun findById(String id);
    Kebun findByName(String name);
    Kebun createKebun(KebunRequestDto requestDTO);
    Kebun deleteKebunById(String id);
    Kebun updateKebun(String id, KebunRequestDto requestDTO);
    Kebun assignMandor(String kebunId, String mandorId);
    Kebun assignSupir(String kebunId, String supirId);
    Kebun removeSupir(String kebunId, String supirId);
    Optional<Kebun>  checkMandorAssignment(String mandorId);
    Optional<Kebun> checkSupirAssignment(String supirId);
}
