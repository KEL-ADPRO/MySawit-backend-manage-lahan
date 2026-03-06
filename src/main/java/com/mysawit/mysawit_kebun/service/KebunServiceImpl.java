package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.repository.KebunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KebunServiceImpl implements KebunService {

    private final KebunRepository kebunRepository;
    private final IdGenerator idGenerator;
    private final OverlapChecker overlapChecker;

    @Override
    public List<Kebun> findAllKebun() {
        return kebunRepository.findAll();
    }

    @Override
    public Kebun findById(String id) {
        UUID uuid = UUID.fromString(id);
        Kebun foundkebun = kebunRepository.findById(uuid).orElse(null);
        if (foundkebun == null) {
            throw new IllegalArgumentException("Kebun with ID " + id + " not found.");
        }
        return foundkebun;
    }

    @Override
    public Kebun findByName(String name) {
        Kebun foundkebun = kebunRepository.findByNama(name).orElse(null);
        if (foundkebun == null) {
            throw new IllegalArgumentException("Kebun with name " + name + " not found.");
        }
        return foundkebun;
    }

    @Override
    public Kebun createKebun(Kebun kebun) {
        List<Kebun> existingKebuns = kebunRepository.findAll();
        for (Kebun existingKebun : existingKebuns) {
            if (overlapChecker.checkOverlap(kebun.getArea(), existingKebun.getArea())) {
                throw new IllegalArgumentException("Kebun overlaps with an existing kebun.");
            }
        }
        return kebunRepository.save(kebun);
    }

    @Override
    public Kebun deleteKebunById(Kebun kebun) {
        kebunRepository.delete(kebun);
        return kebun;
    }

}
