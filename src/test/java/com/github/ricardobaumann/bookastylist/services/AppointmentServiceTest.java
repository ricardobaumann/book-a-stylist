package com.github.ricardobaumann.bookastylist.services;

import com.github.ricardobaumann.bookastylist.models.Appointment;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.repos.AppointmentRepo;
import com.github.ricardobaumann.bookastylist.repos.StylistRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepo appointmentRepo;

    @Mock
    private StylistRepo stylistRepo;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    public void shouldReturnAvailableSlots() {
        //Given
        when(stylistRepo.count()).thenReturn(2L);
        LocalDate date = LocalDate.now();
        Stylist stylist1 = new Stylist(1L, "first", "email1");
        Stylist stylist2 = new Stylist(2L, "second", "email1");
        when(appointmentRepo.findByDate(date))
                .thenReturn(Arrays.asList(
                        new Appointment(1L, stylist1, date, 0),
                        new Appointment(2L, stylist2, date, 0),
                        new Appointment(3L, stylist1, date, 1),
                        new Appointment(4L, stylist2, date, 2),
                        new Appointment(5L, stylist1, date, 3),
                        new Appointment(6L, stylist2, date, 3)
                ));

        //When
        List<LocalDateTime> results = appointmentService.getAvailableSlots(date);

        //Then
        assertThat(results)
                .extracting(LocalDateTime::toString)
                .containsExactlyInAnyOrder(
                        "2019-03-08T09:30",
                        "2019-03-08T10:00",
                        "2019-03-08T11:00",
                        "2019-03-08T11:30",
                        "2019-03-08T12:00",
                        "2019-03-08T12:30",
                        "2019-03-08T13:00",
                        "2019-03-08T13:30",
                        "2019-03-08T14:00",
                        "2019-03-08T14:30",
                        "2019-03-08T15:00",
                        "2019-03-08T15:30",
                        "2019-03-08T16:00",
                        "2019-03-08T16:30");
    }
}