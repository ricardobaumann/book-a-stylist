package com.github.ricardobaumann.bookastylist.services;

import com.github.ricardobaumann.bookastylist.dtos.AvailableSlot;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.projections.SlotCountProjection;
import com.github.ricardobaumann.bookastylist.repos.AppointmentRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
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
        LocalDate date = LocalDate.now().plusDays(1);
        Stylist stylist1 = new Stylist(1L, "first", "email1");
        Stylist stylist2 = new Stylist(2L, "second", "email1");
        when(appointmentRepo.getSlotAppointmentCounts(date))
                .thenReturn(Arrays.asList(
                        new SlotCountProjection(0, 2L),
                        new SlotCountProjection(1, 1L)
                ));

        //When
        List<AvailableSlot> results = appointmentService.getAvailableSlots(date);

        //Then
        assertThat(results)
                .hasSize(15);

        assertThat(results)
                .extracting(AvailableSlot::getSlotNumber)
                .containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
    }

    @Test
    public void shouldReturnEmptyOnAbsentStylists() {
        //Given
        when(stylistService.count()).thenReturn(0L);
        LocalDate date = LocalDate.now().plusDays(1);

        //When //Then
        assertThat(appointmentService.getAvailableSlots(date))
                .isEmpty();
    }

    @Test
    public void shouldReturnEmptyOnPastDate() {
        assertThat(appointmentService.getAvailableSlots(LocalDate.now().minusDays(1)))
                .isEmpty();
    }
}