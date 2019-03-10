package com.github.ricardobaumann.bookastylist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AppointmentResultDto {
    private final StylistDto stylist;
    private final LocalDate date;
    private final Integer slotNumber;
}
