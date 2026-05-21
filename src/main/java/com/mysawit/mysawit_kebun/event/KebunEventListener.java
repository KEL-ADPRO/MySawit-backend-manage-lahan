package com.mysawit.mysawit_kebun.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KebunEventListener {

    @Async("kebunAsyncExecutor")
    @EventListener
    public void handleMandorAssignment(MandorAssignmentEvent event) {
        log.info("Received MandorAssignmentEvent: Mandor {} assigned to Kebun {} ({}) on thread {}",
            event.getMandorId(), event.getKebunId(), event.getNamaKebun(), Thread.currentThread().getName());
    }

    @Async("kebunAsyncExecutor")
    @EventListener
    public void handleMandorRemoval(MandorRemovalEvent event) {
        log.info("Received MandorRemovalEvent: Mandor {} removed from Kebun {} on thread {}",
            event.getMandorId(), event.getKebunId(), Thread.currentThread().getName());
    }

    @Async("kebunAsyncExecutor")
    @EventListener
    public void handleSupirAssignment(SupirAssignmentEvent event) {
        log.info("Received SupirAssignmentEvent: Supir {} assigned to Kebun {} ({}) on thread {}",
            event.getSupirId(), event.getKebunId(), event.getNamaKebun(), Thread.currentThread().getName());
    }

    @Async("kebunAsyncExecutor")
    @EventListener
    public void handleSupirRemoval(SupirRemovalEvent event) {
        log.info("Received SupirRemovalEvent: Supir {} removed from Kebun {} on thread {}",
            event.getSupirId(), event.getKebunId(), Thread.currentThread().getName());
    }
}
