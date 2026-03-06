package com.mysawit.mysawit_kebun.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGenerator {
    public UUID generateUUID() {
        return UUID.randomUUID();
    }
}
