package com.mysawit.mysawit_kebun.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupirRemovalEvent {
    private final String supirId;
    private final String kebunId;
}
