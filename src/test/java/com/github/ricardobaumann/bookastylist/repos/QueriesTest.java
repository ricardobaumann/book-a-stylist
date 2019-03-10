package com.github.ricardobaumann.bookastylist.repos;

import com.github.ricardobaumann.bookastylist.models.Appointment;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class QueriesTest {

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Autowired
    private StylistRepo stylistRepo;

    private LocalDate date = LocalDate.now();
    private Long customerId = 1L;
    private Long customerId2 = 2L;
    private Stylist stylist1 = new Stylist(1L, "first", "mail1@mail.com");
    private Stylist stylist2 = new Stylist(2L, "second", "mail2@mail.com");

    @Before
    public void setUp() {
        appointmentRepo.deleteAll();
        stylistRepo.deleteAll();

        stylist1.setLastAssignedAt(LocalDateTime.now());

        stylist2.setLastAssignedAt(LocalDateTime.now().minusYears(1));

        stylistRepo.saveAll(Arrays.asList(
                stylist1,
                stylist2
        ));

        appointmentRepo.saveAll(Arrays.asList(
                new Appointment(stylist2, date, 0, customerId),
                new Appointment(stylist1, date, 0, customerId2)
        ));
    }

    @Test
    public void shouldFindTopAvailableStylistsFor() {
        Optional<Stylist> result = stylistRepo.findTopByAvailableStylistFor(date, 1, PageRequest.of(0, 1));
        assertThat(result.map(Stylist::getId)).contains(2L);
    }
}