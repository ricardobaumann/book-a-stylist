package com.github.ricardobaumann.bookastylist;

import com.github.ricardobaumann.bookastylist.models.Appointment;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.repos.AppointmentRepo;
import com.github.ricardobaumann.bookastylist.repos.StylistRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
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
            Stylist stylist1 = new Stylist(1L, "first", "email");
            Stylist stylist2 = new Stylist(2L, "second", "email2");
            stylistRepo.saveAll(Arrays.asList(stylist1, stylist2));

            appointmentRepo.save(new Appointment(stylist1, LocalDate.now(), 0, 1L));
            appointmentRepo.save(new Appointment(stylist2, LocalDate.now(), 0, 2L));
        };
    }

}
