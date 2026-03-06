package com.mysawit.mysawit_kebun.service;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IdGeneratorTest {

    @Test
    public void testGenerateId() {
        IdGenerator idGenerator = new IdGenerator();
        UUID id1 = idGenerator.generateUUID();
        UUID id2 = idGenerator.generateUUID();

        assertNotNull(id1);
        assertNotNull(id2);

        assertNotEquals(id1, id2);
    }
}
