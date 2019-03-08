package com.github.ricardobaumann.bookastylist.repos;

import com.github.ricardobaumann.bookastylist.models.Stylist;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface StylistRepo extends CrudRepository<Stylist, Long> {
    @Query("select s from Stylist s " +
            "where not exists (" +
            "   select a from Appointment a" +
            "       where a.stylist = s" +
            "       and a.date = ?1 and a.slotNumber = ?2" +
            ") order by s.lastAssignedAt")
    Optional<Stylist> findTopByAvailableStylistFor(LocalDate date, Integer slotNumber, PageRequest pageRequest);
}
