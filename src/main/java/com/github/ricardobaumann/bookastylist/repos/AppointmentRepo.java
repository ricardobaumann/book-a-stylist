package com.github.ricardobaumann.bookastylist.repos;

import com.github.ricardobaumann.bookastylist.models.Appointment;
import com.github.ricardobaumann.bookastylist.models.Stylist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface AppointmentRepo extends CrudRepository<Appointment, Long> {
    List<Appointment> findByDate(LocalDate date);

    boolean existsByCustomerIdAndDateAndSlotNumber(Long customerId, LocalDate date, Integer slotNumber);

    @Query(nativeQuery = true,
            value = "select s.id, s.name, s.email, s.last_assigned_at from stylist s " +
                    "   where not exists (" +
                    "       select 1 from appointment a " +
                    "         where a.stylist_id = s.id" +
                    "          and a.date = ?1 and a.slot_number = ?2" +
                    ")")
    List<Object[]> queryStylists(LocalDate date, Integer slotNumber);

    default List<Stylist> findAvailableStylistsFor(LocalDate date, Integer slotNumber) {
        return queryStylists(date, slotNumber).stream()
                .map(objects -> new Stylist(
                        ((BigInteger) objects[0]).longValue(),
                        (String) objects[1], (String) objects[2],
                        Optional.ofNullable(objects[3])
                                .map(Timestamp.class::cast)
                                .map(Timestamp::toLocalDateTime).orElse(null)))
                .collect(Collectors.toList());
    }
}
