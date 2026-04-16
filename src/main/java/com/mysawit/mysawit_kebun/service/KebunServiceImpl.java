package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.DTO.KebunRequestDTO;
import com.mysawit.mysawit_kebun.event.MandorAssignmentEvent;
import com.mysawit.mysawit_kebun.event.MandorRemovalEvent;
import com.mysawit.mysawit_kebun.event.SupirAssignmentEvent;
import com.mysawit.mysawit_kebun.event.SupirRemovalEvent;
import com.mysawit.mysawit_kebun.model.Area;
import com.mysawit.mysawit_kebun.model.Kebun;
import com.mysawit.mysawit_kebun.repository.KebunRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KebunServiceImpl implements KebunService {

    private final KebunRepository kebunRepository;
    private final OverlapChecker overlapChecker;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public List<Kebun> findAllKebun() {
        return kebunRepository.findAll();
    }

    @Override
    public Kebun findById(String id) {
        UUID uuid = UUID.fromString(id);
        Kebun foundkebun = kebunRepository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("Kebun with ID " + id + " not found."));
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

    private void createNameValidation(String name) {
        if (kebunRepository.existsByNama(name)) {
            throw new IllegalArgumentException("Kebun with name " + name + " already exists.");
        }
    }

    private void createOverlapValidation(Area newArea) {
        List<Kebun> existingKebuns = kebunRepository.findAll();
        for (Kebun existingKebun : existingKebuns) {
            if (overlapChecker.checkOverlap(newArea, existingKebun.getArea())) {
                throw new IllegalArgumentException("Kebun overlaps with an existing kebun.");
            }
        }
    }

    @Override
    public Kebun createKebun(KebunRequestDTO requestDTO) {
        createNameValidation(requestDTO.getNama());

        Area newArea = requestDTO.getArea().toEntity();
        createOverlapValidation(newArea);

        Kebun kebun = new Kebun();
        kebun.setNama(requestDTO.getNama());
        kebun.setLuas(requestDTO.getLuas());
        kebun.setArea(newArea);

        return kebunRepository.save(kebun);
    }

    @Override
    public Kebun deleteKebunById(String id) {
        Kebun existingKebun = findById(id);
        kebunRepository.delete(existingKebun);
        return existingKebun;
    }

    private void updateNameValidation(Kebun existingKebun, String newName) {
        if (!existingKebun.getNama().equals(newName) && kebunRepository.existsByNama(newName)) {
            throw new IllegalArgumentException("Kebun with name " + newName + " already exists.");
        }
    }

    private void updateOverlapValidation(Area newArea, UUID existingKebunId) {
        List<Kebun> existingKebuns = kebunRepository.findAll();
        for (Kebun otherKebun: existingKebuns) {
            if (!otherKebun.getId().equals(existingKebunId) && overlapChecker.checkOverlap(newArea, otherKebun.getArea())) {
                throw new IllegalArgumentException("Updated kebun overlaps with an existing kebun.");
            }
        }
    }

    @Override
    public Kebun updateKebun(String id, KebunRequestDTO requestDTO) {
        Kebun existingKebun = findById(id);

        updateNameValidation(existingKebun, requestDTO.getNama());

        if (requestDTO.getArea() != null) {
            Area updatedArea = requestDTO.getArea().toEntity();
            updateOverlapValidation(updatedArea, existingKebun.getId());
            existingKebun.setArea(updatedArea);
        }

        existingKebun.setNama(requestDTO.getNama());
        existingKebun.setLuas(requestDTO.getLuas());

        return kebunRepository.save(existingKebun);
    }

    private void validateTargetKebunAvailability(Kebun targetKebun, String mandorId) {
        if (targetKebun.getMandorId() != null && !targetKebun.getMandorId().equals(mandorId)) {
            throw new IllegalArgumentException("Kebun already has a different Mandor assigned. Reassign that Mandor first.");
        }
    }

    private void removeMandorFromOldKebun(Kebun oldKebun) {
        String mandorIdToRemove = oldKebun.getMandorId();
        oldKebun.setMandorId(null);
        kebunRepository.save(oldKebun);

        applicationEventPublisher.publishEvent(new MandorRemovalEvent(mandorIdToRemove, oldKebun.getId().toString()));
    }

    @Override
    @Transactional
    public Kebun assignMandor(String targetKebunId, String mandorId) {
        Kebun targetKebun = findById(targetKebunId);

        if (mandorId.equals(targetKebun.getMandorId())) {
            return targetKebun;
        }

        validateTargetKebunAvailability(targetKebun, mandorId);

        Optional<Kebun> existingMandorAssignment = kebunRepository.findByMandorId(mandorId);
        existingMandorAssignment.ifPresent(this::removeMandorFromOldKebun);

        targetKebun.setMandorId(mandorId);
        Kebun savedKebun = kebunRepository.save(targetKebun);
        applicationEventPublisher.publishEvent(new MandorAssignmentEvent(mandorId, targetKebunId, savedKebun.getNama()));

        return savedKebun;
    }

    private void removeSupirFromOldKebun(Kebun oldKebun, String supirId) {
        oldKebun.getSupirIds().remove(supirId);
        kebunRepository.save(oldKebun);
        applicationEventPublisher.publishEvent(new SupirRemovalEvent(supirId, oldKebun.getId().toString()));
    }

    @Override
    @Transactional
    public Kebun assignSupir(String targetKebunId, String supirId) {
        Kebun targetKebun = findById(targetKebunId);

        if (targetKebun.getSupirIds() == null) {
            targetKebun.setSupirIds(new ArrayList<>());
        }

        if (targetKebun.getSupirIds().contains(supirId)) {
            return targetKebun;
        }

        Optional<Kebun> existingSupirAssignment = kebunRepository.findBySupirIdsContaining(supirId);
        existingSupirAssignment.ifPresent(oldKebun -> removeSupirFromOldKebun(oldKebun, supirId));

        targetKebun.getSupirIds().add(supirId);
        Kebun savedKebun = kebunRepository.save(targetKebun);

        applicationEventPublisher.publishEvent(new SupirAssignmentEvent(supirId, targetKebunId, savedKebun.getNama()));

        return savedKebun;
    }

    @Override
    public Kebun removeSupir(String kebunId, String supirId) {
        Kebun existingKebun = findById(kebunId);

        if (!existingKebun.getSupirIds().contains(supirId)) {
            throw new IllegalArgumentException("Supir Truk is not assigned to this kebun.");
        }

        existingKebun.getSupirIds().remove(supirId);

        SupirRemovalEvent event = new SupirRemovalEvent(supirId, kebunId);
        applicationEventPublisher.publishEvent(event);

        return kebunRepository.save(existingKebun);
    }
}
