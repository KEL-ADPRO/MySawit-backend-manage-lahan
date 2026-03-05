package com.mysawit.mysawit_kebun.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lahan")
@Getter
@Setter
public class Kebun {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nama;

    private double luas;

    @Embedded
    private Area area;
}
