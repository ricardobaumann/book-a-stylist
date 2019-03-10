package com.github.ricardobaumann.bookastylist.services;

import com.github.ricardobaumann.bookastylist.exceptions.CustomerAlreadyBookedException;
import com.github.ricardobaumann.bookastylist.exceptions.PastDateAppointmentException;
import com.github.ricardobaumann.bookastylist.exceptions.SlotUnavailableException;
import com.github.ricardobaumann.bookastylist.models.Appointment;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.repos.AppointmentRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        when(stylistService.findAvailableStylist(date, slotNumber))
                .thenReturn(Optional.of(stylist));

        Appointment appointment = new Appointment(stylist, date, slotNumber, customerId);
        when(appointmentRepo.save(any())).thenReturn(appointment);

        doNothing().when(stylistService).wasAssignedAt(any(), any());

        //When //Then
        assertThat(appointmentService.bookCustomerAt(customerId, date, slotNumber))
                .isEqualTo(appointment);

        ArgumentCaptor<Appointment> appointmentArgumentCaptor = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepo).save(appointmentArgumentCaptor.capture());
        Appointment capturedAppointment = appointmentArgumentCaptor.getValue();
        assertThat(capturedAppointment.getStylist()).isEqualTo(stylist);
        assertThat(capturedAppointment.getSlotNumber()).isEqualTo(slotNumber);
        assertThat(capturedAppointment.getDate()).isEqualTo(date);
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

        when(stylistService.findAvailableStylist(date, slotNumber))
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