package com.github.ricardobaumann.bookastylist.services;

import com.github.ricardobaumann.bookastylist.exceptions.CustomerAlreadyBookedException;
import com.github.ricardobaumann.bookastylist.exceptions.PastDateAppointmentException;
import com.github.ricardobaumann.bookastylist.exceptions.SlotUnavailableException;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.repos.AppointmentRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

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
        LocalDate date = LocalDate.now().plusDays(1);
        Integer slotNumber = 1;

        when(appointmentRepo.existsByCustomerIdAndDateAndSlotNumber(
                customerId,
                date,
                slotNumber
        )).thenReturn(false);

        Stylist stylist = new Stylist(1L, "name", "mail");
        when(stylistService.findTopAvailableStylistsFor(date, slotNumber))
                .thenReturn(Optional.of(stylist));

        doNothing().when(stylistService).wasAssignedAt(any(), any());

        //When //Then
        appointmentService.bookCustomerAt(customerId, date, slotNumber);
    }

    @Test
    public void shouldThrowExceptionOnCustomerAlreadyBooked() {
        //Given
        Long customerId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);
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
        LocalDate date = LocalDate.now().plusDays(1);
        Integer slotNumber = 1;

        when(appointmentRepo.existsByCustomerIdAndDateAndSlotNumber(
                customerId,
                date,
                slotNumber
        )).thenReturn(false);

        when(stylistService.findTopAvailableStylistsFor(date, slotNumber))
                .thenReturn(Optional.empty());

        //When //Then
        assertThatThrownBy(() -> appointmentService.bookCustomerAt(customerId, date, slotNumber))
                .isInstanceOf(SlotUnavailableException.class);
    }

    @Test
    public void shouldThrowExceptionOnPastBookingDate() {
        //Given
        Long customerId = 1L;
        LocalDate date = LocalDate.now().minusDays(1);
        Integer slotNumber = 1;

        //When //Then
        assertThatThrownBy(() -> appointmentService.bookCustomerAt(customerId, date, slotNumber))
                .isInstanceOf(PastDateAppointmentException.class);
    }
}