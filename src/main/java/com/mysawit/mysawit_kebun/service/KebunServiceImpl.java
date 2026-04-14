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
        if (kebunRepository.existsByNama(kebun.getNama())) {
            throw new IllegalArgumentException("Kebun with name " + kebun.getNama() + " already exists.");
        }

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

    @Override
    public Kebun updateKebun(String id, Kebun updatedData) {
        UUID uuid = UUID.fromString(id);
        Kebun existingKebun = kebunRepository.findById(uuid).orElse(null);

        if (existingKebun == null) {
            throw new IllegalArgumentException("Kebun with ID " + id + " not found.");
        }

        if (!existingKebun.getNama().equals(updatedData.getNama()) && kebunRepository.existsByNama(updatedData.getNama())) {
            throw new IllegalArgumentException("Kebun with name " + updatedData.getNama() + " already exists.");
        }

        if (updatedData.getArea() != null) {
            List<Kebun> existingKebuns = kebunRepository.findAll();
            for (Kebun otherKebun: existingKebuns) {
                if (!otherKebun.getId().equals(existingKebun.getId()) && overlapChecker.checkOverlap(updatedData.getArea(), otherKebun.getArea())) {
                    throw new IllegalArgumentException("Updated kebun overlaps with an existing kebun.");
                }
            }
        }

        existingKebun.setNama(updatedData.getNama());
        existingKebun.setLuas(updatedData.getLuas());
        if (updatedData.getArea() != null) {
            existingKebun.setArea(updatedData.getArea());
        }

        return kebunRepository.save(existingKebun);
    }

}
