package com.github.ricardobaumann.bookastylist.services;

import com.github.ricardobaumann.bookastylist.exceptions.DuplicatedStylistException;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import com.github.ricardobaumann.bookastylist.repos.StylistRepo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StylistServiceTest {

    @Mock
    private StylistRepo stylistRepo;

    @InjectMocks
    private StylistService stylistService;

    @Test
    public void shouldSaveStylist() {
        //Given
        Stylist stylist = new Stylist(1L, "name", "email");
        when(stylistRepo.save(stylist))
                .thenReturn(stylist);

        //When
        stylistService.save(stylist);

        //Then
        verify(stylistRepo).save(stylist);
    }

    @Test
    public void shouldThrowExceptionOnConstraintViolation() {
        //Given
        Stylist stylist = new Stylist(1L, "name", "email");
        when(stylistRepo.save(stylist))
                .thenThrow(DataIntegrityViolationException.class);

        //When //Then
        Assertions.assertThatThrownBy(() -> stylistService.save(stylist))
                .isInstanceOf(DuplicatedStylistException.class);
    }
}