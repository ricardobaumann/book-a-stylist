package com.github.ricardobaumann.bookastylist.services;

import com.github.ricardobaumann.bookastylist.exceptions.CustomerAlreadyBookedException;
import com.github.ricardobaumann.bookastylist.exceptions.SlotUnavailableException;
import com.github.ricardobaumann.bookastylist.models.Appointment;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.repos.AppointmentRepo;
import com.github.ricardobaumann.bookastylist.repos.StylistRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class AppointmentService {

    private static final long SLOT_SIZE = 30;
    private static final AtomicInteger ZERO = new AtomicInteger(0);
    private final int MAX_SLOTS_PER_DAY = 16;
    private final int STARTING_HOUR = 9;

    private final AppointmentRepo appointmentRepo;
    private final StylistRepo stylistRepo;

    public AppointmentService(AppointmentRepo appointmentRepo,
                              StylistRepo stylistRepo) {
        this.appointmentRepo = appointmentRepo;
        this.stylistRepo = stylistRepo;
    }

    public List<LocalDateTime> getAvailableSlots(LocalDate date) {
        Map<Integer, AtomicInteger> slotsBySlotNumber = new HashMap<>();
        long stylistsAmount = stylistRepo.count();
        appointmentRepo.findByDate(date)
                .forEach(appointment -> slotsBySlotNumber.computeIfAbsent(appointment.getSlotNumber(),
                        integer -> new AtomicInteger())
                        .incrementAndGet());

        AtomicReference<LocalDateTime> startingTime = new AtomicReference<>(
                LocalDateTime.of(date,
                        LocalTime.of(STARTING_HOUR, 0)));

        return IntStream.range(0, MAX_SLOTS_PER_DAY)
                .mapToObj(value -> {
                    LocalDateTime dateTime = startingTime.get();
                    startingTime.set(startingTime.get().plusMinutes(SLOT_SIZE));
                    if (slotsBySlotNumber.getOrDefault(value, ZERO).get() >= stylistsAmount) {
                        return null;
                    } else {
                        return dateTime;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void bookCustomerAt(Long customerId, LocalDate date, Integer slotNumber) {
        if (appointmentRepo.existsByCustomerIdAndDateAndSlotNumber(customerId, date, slotNumber)) {
            throw new CustomerAlreadyBookedException();
        }
        List<Stylist> stylists = appointmentRepo.findAvailableStylistsFor(date, slotNumber);

        log.info("Available stylists: {}", stylists);
        if (stylists.isEmpty()) {
            throw new SlotUnavailableException();
        }

        Stylist stylist = stylists.get(0);
        appointmentRepo.save(new Appointment(stylist, date, slotNumber, customerId));
    }
}
