package com.github.ricardobaumann.bookastylist.services;

import com.github.ricardobaumann.bookastylist.exceptions.CustomerAlreadyBookedException;
import com.github.ricardobaumann.bookastylist.exceptions.SlotUnavailableException;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.repos.AppointmentRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceBookTest {

    @Mock
    private AppointmentRepo appointmentRepo;

    @Mock
    private StylistService stylistService;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    public void shouldBookCustomer() {
        //Given
        Long customerId = 1L;
        LocalDate date = LocalDate.now();
        Integer slotNumber = 1;

        when(appointmentRepo.existsByCustomerIdAndDateAndSlotNumber(
                customerId,
                date,
                slotNumber
        )).thenReturn(false);

        Stylist stylist = new Stylist(1L, "name", "mail");
        when(appointmentRepo.findAvailableStylistsFor(date, slotNumber))
                .thenReturn(Collections.singletonList(
                        stylist));

        doNothing().when(stylistService).wasAssignedAt(any(), any());

        //When //Then
        appointmentService.bookCustomerAt(customerId, date, slotNumber);
    }

    @Test
    public void shouldThrowExceptionOnCustomerAlreadyBooked() {
        //Given
        Long customerId = 1L;
        LocalDate date = LocalDate.now();
        Integer slotNumber = 1;

        when(appointmentRepo.existsByCustomerIdAndDateAndSlotNumber(
                customerId,
                date,
                slotNumber
        )).thenReturn(true);

        //When //Then
        assertThatThrownBy(() -> appointmentService.bookCustomerAt(
                customerId,
                date,
                slotNumber
        )).isInstanceOf(CustomerAlreadyBookedException.class);
    }

    @Test
    public void shouldThrowExceptionOnNonAvailableStylist() {
        //Given
        Long customerId = 1L;
        LocalDate date = LocalDate.now();
        Integer slotNumber = 1;

        when(appointmentRepo.existsByCustomerIdAndDateAndSlotNumber(
                customerId,
                date,
                slotNumber
        )).thenReturn(false);

        when(appointmentRepo.findAvailableStylistsFor(date, slotNumber))
                .thenReturn(Collections.emptyList());

        //When //Then
        assertThatThrownBy(() -> appointmentService.bookCustomerAt(customerId, date, slotNumber))
                .isInstanceOf(SlotUnavailableException.class);
    }
}