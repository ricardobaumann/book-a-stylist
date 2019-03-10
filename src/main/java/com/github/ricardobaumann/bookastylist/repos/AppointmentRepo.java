package com.github.ricardobaumann.bookastylist.repos;

import com.github.ricardobaumann.bookastylist.models.Appointment;
import com.github.ricardobaumann.bookastylist.projections.SlotCountProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepo extends CrudRepository<Appointment, Long> {

    boolean existsByCustomerIdAndDateAndSlotNumber(Long customerId, LocalDate date, Integer slotNumber);

    @Query("select new com.github.ricardobaumann.bookastylist.projections.SlotCountProjection(a.slotNumber, count(a))" +
            " from Appointment  a " +
            "   where a.date = ?1 " +
            "group by a.slotNumber")
    List<SlotCountProjection> getSlotAppointmentCounts(LocalDate date);

}
