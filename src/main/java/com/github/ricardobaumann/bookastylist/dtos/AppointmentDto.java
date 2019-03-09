package com.github.ricardobaumann.bookastylist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {

    @NotNull
    private Long customerId;

    @NotNull
    private LocalDate date;

    @NotNull
    @Min(0)
    @Max(15)
    private Integer slotNumber;
}
