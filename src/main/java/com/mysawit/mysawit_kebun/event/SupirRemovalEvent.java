package com.mysawit.mysawit_kebun.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SupirRemovalEvent {
    private final String supirId;
    private final String kebunId;
}
