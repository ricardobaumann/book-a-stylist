package com.github.ricardobaumann.bookastylist.controllers;

import com.github.ricardobaumann.bookastylist.dtos.StylistDto;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.services.StylistService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
public class StylistController {

    private final StylistService stylistService;

    public StylistController(StylistService stylistService) {
        this.stylistService = stylistService;
    }

    @PutMapping("/stylists/{id}")
    public void put(@PathVariable Long id, @RequestBody @Valid StylistDto stylistDto) {
        stylistService.save(new Stylist(id, stylistDto.getName(), stylistDto.getEmail()));
    }
}
