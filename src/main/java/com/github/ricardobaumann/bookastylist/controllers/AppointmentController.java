package com.github.ricardobaumann.bookastylist.controllers;

import com.github.ricardobaumann.bookastylist.services.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
