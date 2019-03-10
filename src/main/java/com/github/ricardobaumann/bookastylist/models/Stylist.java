package com.github.ricardobaumann.bookastylist.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Stylist {

    @Id
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Column(unique = true)
    @Email
    private String email;

    private LocalDateTime lastAssignedAt;

    public Stylist(Long id, @NotNull String name, @NotNull String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Stylist(Long id, @NotNull String name, @NotNull String email, LocalDateTime lastAssignedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.lastAssignedAt = lastAssignedAt;
    }
}
