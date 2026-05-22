package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.event.MandorAssignmentEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "jwt.secret=mysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkey"
})
public class KebunAsyncTest {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private TestAsyncListener testAsyncListener;

    @Test
    public void testEventExecutesAsynchronously() throws InterruptedException {
        MandorAssignmentEvent event = new MandorAssignmentEvent("mandor-1", "kebun-1", "Kebun A");

        eventPublisher.publishEvent(event);

        boolean completed = testAsyncListener.getLatch().await(5, TimeUnit.SECONDS);
        assertTrue(completed, "Async event listener did not finish in time");

        String threadName = testAsyncListener.getExecutedThreadName();
        assertNotNull(threadName);

        assertTrue(threadName.startsWith("KebunAsync-"),
            "Expected thread name to start with 'KebunAsync-', but was: " + threadName);
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public TestAsyncListener testAsyncListener() {
            return new TestAsyncListener();
        }
    }

    public static class TestAsyncListener {
        private final CountDownLatch latch = new CountDownLatch(1);
        private final AtomicReference<String> executedThreadName = new AtomicReference<>();

        public CountDownLatch getLatch() {
            return latch;
        }

        public String getExecutedThreadName() {
            return executedThreadName.get();
        }

        @Async("kebunAsyncExecutor")
        @EventListener
        public void handleEvent(MandorAssignmentEvent event) {
            executedThreadName.set(Thread.currentThread().getName());
            latch.countDown();
        }
    }
}
