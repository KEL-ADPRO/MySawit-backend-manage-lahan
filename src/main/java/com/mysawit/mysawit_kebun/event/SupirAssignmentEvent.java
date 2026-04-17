package com.mysawit.mysawit_kebun.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SupirAssignmentEvent {
    private final String supirId;
    private final String kebunId;
    private final String namaKebun;
}
