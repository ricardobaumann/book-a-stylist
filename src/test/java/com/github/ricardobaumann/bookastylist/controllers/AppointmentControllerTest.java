package com.github.ricardobaumann.bookastylist.controllers;

import com.github.ricardobaumann.bookastylist.dtos.AvailableSlot;
import com.github.ricardobaumann.bookastylist.services.AppointmentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController).build();
    }

    @Test
    public void shouldGetAvailableSlots() throws Exception {
        //Given
        LocalDate date = LocalDate.now();
        LocalDateTime dateTime = LocalDateTime.now();
        Mockito.when(appointmentService.getAvailableSlots(date))
                .thenReturn(Collections.singletonList(
                        new AvailableSlot(0, dateTime)));

        //When //Then
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        mockMvc.perform(get("/slots/available/{date}", date.format(formatter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].slotNumber", is(0)))
                .andExpect(jsonPath("$[0].dateTime", is(notNullValue())));
    }
}