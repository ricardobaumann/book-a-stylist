package com.github.ricardobaumann.bookastylist.services;

import com.github.ricardobaumann.bookastylist.exceptions.DuplicatedStylistException;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.repos.StylistRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class StylistService {

    private final StylistRepo stylistRepo;

    public StylistService(StylistRepo stylistRepo) {
        this.stylistRepo = stylistRepo;
    }

    public void save(Stylist stylist) {
        try {
            stylistRepo.save(stylist);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedStylistException();
        }
    }
}
