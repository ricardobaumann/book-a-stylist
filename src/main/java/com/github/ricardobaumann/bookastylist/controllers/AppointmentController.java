package com.github.ricardobaumann.bookastylist.controllers;

import com.github.ricardobaumann.bookastylist.AppointmentDto;
import com.github.ricardobaumann.bookastylist.services.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/slots/available/{date}")
    public List<LocalDateTime> getAvailableSlots(@PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        return appointmentService.getAvailableSlots(date);
    }

    @PostMapping("/appointments")
    public void post(@RequestBody AppointmentDto appointmentDto) {
        appointmentService.bookCustomerAt(
                appointmentDto.getCustomerId(),
                appointmentDto.getDate(),
                appointmentDto.getSlotNumber()
        );
    }
}
