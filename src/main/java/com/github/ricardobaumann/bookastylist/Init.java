package com.github.ricardobaumann.bookastylist;

import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.repos.AppointmentRepo;
import com.github.ricardobaumann.bookastylist.repos.StylistRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class Init {

    private final StylistRepo stylistRepo;
    private final AppointmentRepo appointmentRepo;

    public Init(StylistRepo stylistRepo, AppointmentRepo appointmentRepo) {
        this.stylistRepo = stylistRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            Stylist stylist1 = new Stylist(1L, "Default stylist 1", "email@mail");
            Stylist stylist2 = new Stylist(2L, "Default stylist 2", "email2@mail");
            stylistRepo.saveAll(Arrays.asList(stylist1, stylist2));
        };
    }

}
