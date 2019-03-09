package com.github.ricardobaumann.bookastylist.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AvailableSlot {
    private final Integer slotNumber;
    private final LocalDateTime dateTime;
}
