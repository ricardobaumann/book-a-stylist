package com.github.ricardobaumann.bookastylist.services;

import com.github.ricardobaumann.bookastylist.dtos.AvailableSlot;
import com.github.ricardobaumann.bookastylist.exceptions.CustomerAlreadyBookedException;
import com.github.ricardobaumann.bookastylist.exceptions.SlotUnavailableException;
import com.github.ricardobaumann.bookastylist.models.Appointment;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.repos.AppointmentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class AppointmentService {

    private static final long SLOT_SIZE = 30;
    private static final AtomicInteger ZERO = new AtomicInteger(0);

    private final AppointmentRepo appointmentRepo;
    private final StylistService stylistService;

    public AppointmentService(AppointmentRepo appointmentRepo,
                              StylistService stylistService) {
        this.appointmentRepo = appointmentRepo;
        this.stylistService = stylistService;
    }

    public List<AvailableSlot> getAvailableSlots(LocalDate date) {
        //could also include customerId to filter out customer booked slots
        Map<Integer, AtomicInteger> slotsBySlotNumber = new HashMap<>();
        long stylistsAmount = stylistService.count();
        appointmentRepo.findByDate(date)
                .forEach(appointment -> slotsBySlotNumber.computeIfAbsent(appointment.getSlotNumber(),
                        integer -> new AtomicInteger())
                        .incrementAndGet());

        AtomicReference<LocalDateTime> startingTime = new AtomicReference<>(
                LocalDateTime.of(date,
                        LocalTime.of(9, 0)));

        return IntStream.range(0, 16)
                .mapToObj(value -> {
                    LocalDateTime dateTime = startingTime.get();
                    startingTime.set(startingTime.get().plusMinutes(SLOT_SIZE));
                    if (slotsBySlotNumber.getOrDefault(value, ZERO).get() >= stylistsAmount) {
                        return null;
                    } else {
                        return new AvailableSlot(value, dateTime);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional
    public void bookCustomerAt(Long customerId, LocalDate date, Integer slotNumber) {
        if (appointmentRepo.existsByCustomerIdAndDateAndSlotNumber(customerId, date, slotNumber)) {
            throw new CustomerAlreadyBookedException();
        }
        Optional<Stylist> stylistOptional = stylistService.findTopAvailableStylistsFor(date, slotNumber);

        stylistOptional.ifPresentOrElse(stylist -> {
            log.info("Assigned {}", stylist);
            appointmentRepo.save(new Appointment(stylist, date, slotNumber, customerId));
            stylistService.wasAssignedAt(stylist, LocalDateTime.now());
        }, () -> {
            throw new SlotUnavailableException();
        });
    }
}
