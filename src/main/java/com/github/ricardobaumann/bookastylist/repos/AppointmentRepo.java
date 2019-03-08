package com.github.ricardobaumann.bookastylist.repos;

import com.github.ricardobaumann.bookastylist.models.Appointment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepo extends CrudRepository<Appointment, Long> {
    List<Appointment> findByDate(LocalDate date);

    boolean existsByCustomerIdAndDateAndSlotNumber(Long customerId, LocalDate date, Integer slotNumber);

}
