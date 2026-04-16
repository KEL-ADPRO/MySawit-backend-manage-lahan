package com.mysawit.mysawit_kebun.repository;

import com.mysawit.mysawit_kebun.model.Kebun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KebunRepository extends JpaRepository<Kebun, UUID> {
    public Optional<Kebun> findByNama(String nama);
    public boolean existsByNama(String nama);
    public Optional<Kebun> findByMandorId(String mandorId);
}
