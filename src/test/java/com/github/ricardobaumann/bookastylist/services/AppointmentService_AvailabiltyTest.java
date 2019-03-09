package com.github.ricardobaumann.bookastylist.services;

import com.github.ricardobaumann.bookastylist.dtos.AvailableSlot;
import com.github.ricardobaumann.bookastylist.models.Appointment;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.repos.AppointmentRepo;
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
public class AppointmentService_AvailabiltyTest {

    @Mock
    private AppointmentRepo appointmentRepo;

    @Mock
    private StylistService stylistService;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    public void shouldReturnAvailableSlots() {
        //Given
        when(stylistService.count()).thenReturn(2L);
        LocalDate date = LocalDate.of(2019, 3, 8);
        Stylist stylist1 = new Stylist(1L, "first", "email1");
        Stylist stylist2 = new Stylist(2L, "second", "email1");
        when(appointmentRepo.findByDate(date))
                .thenReturn(Arrays.asList(
                        new Appointment(stylist1, date, 0, 1L),
                        new Appointment(stylist2, date, 0, 1L),
                        new Appointment(stylist1, date, 1, 1L),
                        new Appointment(stylist2, date, 2, 1L),
                        new Appointment(stylist1, date, 3, 1L),
                        new Appointment(stylist2, date, 3, 1L)
                ));

        //When
        List<AvailableSlot> results = appointmentService.getAvailableSlots(date);

        //Then
        assertThat(results)
                .extracting(AvailableSlot::getDateTime)
                .extracting(LocalDateTime::toString)
                .containsExactly(
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

        assertThat(results)
                .extracting(AvailableSlot::getSlotNumber)
                .containsExactly(1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
    }

    @Test
    public void shouldReturnEmptyOnAbsentStylists() {
        //Given
        when(stylistService.count()).thenReturn(0L);
        LocalDate date = LocalDate.of(2019, 3, 8);

        //When //Then
        assertThat(appointmentService.getAvailableSlots(date))
                .isEmpty();
    }
}