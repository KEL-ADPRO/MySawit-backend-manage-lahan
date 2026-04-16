package com.mysawit.mysawit_kebun.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MandorRemovalEvent {
    private final String mandorId;
    private final String kebunId;
}
