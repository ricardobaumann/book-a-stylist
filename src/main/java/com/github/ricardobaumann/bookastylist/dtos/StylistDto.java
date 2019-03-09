package com.github.ricardobaumann.bookastylist.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StylistDto {

    @NotNull
    private String name;

    @NotNull
    private String email;
}
