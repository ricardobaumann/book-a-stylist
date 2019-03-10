package com.github.ricardobaumann.bookastylist.services;

import com.github.ricardobaumann.bookastylist.exceptions.DuplicatedStylistException;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.repos.StylistRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StylistService {

    private final StylistRepo stylistRepo;

    public StylistService(StylistRepo stylistRepo) {
        this.stylistRepo = stylistRepo;
    }

    public void save(Stylist stylist) {
        try {
            stylist.setLastAssignedAt(LocalDateTime.now());
            stylistRepo.save(stylist);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedStylistException();
        }
    }

    long count() {
        return stylistRepo.count();
    }

    void wasAssignedAt(Stylist stylist, LocalDateTime dateTime) {
        stylist.setLastAssignedAt(dateTime);
        stylistRepo.save(stylist);
    }

    Optional<Stylist> findAvailableStylist(LocalDate date, Integer slotNumber) {
        return stylistRepo.findTopByAvailableStylistFor(date, slotNumber, PageRequest.of(0, 1));
    }
}
