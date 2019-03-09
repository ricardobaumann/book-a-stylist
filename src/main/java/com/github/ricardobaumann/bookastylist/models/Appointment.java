package com.github.ricardobaumann.bookastylist.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_app_stylist_date_slot",
                columnNames = {"stylist_id", "date", "slotNumber"}),
        @UniqueConstraint(
                name = "uk_app_cust_date_slot",
                columnNames = {"customerId", "date", "slotNumber"}
        )
})
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Stylist stylist;

    @NotNull
    private LocalDate date;

    @NotNull
    @Min(0)
    @Max(15)
    private Integer slotNumber;

    @NotNull
    private Long customerId;

    public Appointment(Stylist stylist,
                       @NotNull LocalDate date,
                       @NotNull @Min(0) @Max(15) Integer slotNumber,
                       @NotNull Long customerId) {
        this.stylist = stylist;
        this.date = date;
        this.slotNumber = slotNumber;
        this.customerId = customerId;
    }
}
