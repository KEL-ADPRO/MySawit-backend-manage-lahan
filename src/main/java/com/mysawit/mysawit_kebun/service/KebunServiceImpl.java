package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.repository.KebunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KebunServiceImpl implements KebunService {

    @Autowired
    private KebunRepository kebunRepository;

    @Override
    public List<Kebun> findAllKebun() {
        return kebunRepository.findAll();
    }

    @Override
    public Kebun createKebun(Kebun kebun) {
        return kebunRepository.save(kebun);
    }

}
