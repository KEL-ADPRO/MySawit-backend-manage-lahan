package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.model.Kebun;

import java.util.List;

public interface KebunService {
    List<Kebun> getAllKebun();
    Kebun createKebun(Kebun kebun);
}
