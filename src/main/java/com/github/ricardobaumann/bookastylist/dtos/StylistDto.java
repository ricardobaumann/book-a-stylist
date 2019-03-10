package com.github.ricardobaumann.bookastylist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StylistDto {

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;
}
