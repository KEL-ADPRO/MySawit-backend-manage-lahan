package com.mysawit.mysawit_kebun.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MandorAssignmentEvent {
    private final String mandorId;
    private final String kebunId;
    private final String namaKebun;
}
