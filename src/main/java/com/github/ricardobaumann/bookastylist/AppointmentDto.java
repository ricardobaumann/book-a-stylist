package com.github.ricardobaumann.bookastylist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    private Long customerId;
    private LocalDate date;
    private Integer slotNumber;
}
