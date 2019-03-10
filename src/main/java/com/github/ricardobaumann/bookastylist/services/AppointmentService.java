package com.github.ricardobaumann.bookastylist.services;

import com.github.ricardobaumann.bookastylist.dtos.AvailableSlot;
import com.github.ricardobaumann.bookastylist.exceptions.CustomerAlreadyBookedException;
import com.github.ricardobaumann.bookastylist.exceptions.PastDateAppointmentException;
import com.github.ricardobaumann.bookastylist.exceptions.SlotUnavailableException;
import com.github.ricardobaumann.bookastylist.models.Appointment;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.projections.SlotCountProjection;
import com.github.ricardobaumann.bookastylist.repos.AppointmentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class AppointmentService {

    private static final long SLOT_SIZE = 30;
    private static final int STARTING_HOUR = 9;

    private final AppointmentRepo appointmentRepo;
    private final StylistService stylistService;

    public AppointmentService(AppointmentRepo appointmentRepo,
                              StylistService stylistService) {
        this.appointmentRepo = appointmentRepo;
        this.stylistService = stylistService;
    }

    public List<AvailableSlot> getAvailableSlots(LocalDate date) {
        //could also include customerId to filter out customer booked slots
        if (date.isBefore(LocalDate.now())) {
            return Collections.emptyList();
        }

        long stylistsAmount = stylistService.count();
        if (stylistsAmount == 0) {
            log.warn("There are no available stylists");
            return Collections.emptyList();
        }

        Map<Integer, Long> slotsBySlotNumber = appointmentRepo.getSlotAppointmentCounts(date)
                .stream()
                .collect(Collectors.toMap(SlotCountProjection::getSlotNumber, SlotCountProjection::getCount));

        AtomicReference<LocalDateTime> startingTime = new AtomicReference<>(
                LocalDateTime.of(date,
                        LocalTime.of(STARTING_HOUR, 0)));

        return IntStream.range(0, 16)
                .mapToObj(value -> {
                    LocalDateTime dateTime = startingTime.get();
                    startingTime.set(startingTime.get().plusMinutes(SLOT_SIZE));
                    if (dateTime.isBefore(LocalDateTime.now())) {
                        return null;
                    }
                    if (slotsBySlotNumber.getOrDefault(value, 0L) >= stylistsAmount) {
                        return null;
                    } else {
                        return new AvailableSlot(value, dateTime);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional
    public Appointment bookCustomerAt(Long customerId, LocalDate date, Integer slotNumber) {
        LocalDateTime slotDateTime = LocalDateTime.of(date, LocalTime.of(STARTING_HOUR, 0))
                .plusMinutes(slotNumber * SLOT_SIZE);
        log.info("Slot date time {}", slotDateTime);
        if (slotDateTime.isBefore(LocalDateTime.now())) {
            throw new PastDateAppointmentException();
        }
        if (appointmentRepo.existsByCustomerIdAndDateAndSlotNumber(customerId, date, slotNumber)) {
            throw new CustomerAlreadyBookedException();
        }
        Optional<Stylist> stylistOptional = stylistService.findAvailableStylist(date, slotNumber);

        return stylistOptional
                .map(stylist -> {
                    log.info("Assigned {} at {} slot {}", stylist, date, slotNumber);
                    stylistService.wasAssignedAt(stylist, LocalDateTime.now());
                    return appointmentRepo.save(new Appointment(stylist, date, slotNumber, customerId));
                }).orElseThrow(SlotUnavailableException::new);
    }
}
