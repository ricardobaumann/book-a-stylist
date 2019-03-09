package com.github.ricardobaumann.bookastylist.controllers;

import com.github.ricardobaumann.bookastylist.dtos.AppointmentDto;
import com.github.ricardobaumann.bookastylist.dtos.AvailableSlot;
import com.github.ricardobaumann.bookastylist.services.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/slots/available/{date}")
    public List<AvailableSlot> getAvailableSlots(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return appointmentService.getAvailableSlots(date);
    }

    @PostMapping("/appointments")
    public void post(@RequestBody @Valid AppointmentDto appointmentDto) {
        appointmentService.bookCustomerAt(
                appointmentDto.getCustomerId(),
                appointmentDto.getDate(),
                appointmentDto.getSlotNumber()
        );
    }
}
