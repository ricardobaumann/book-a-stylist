package com.github.ricardobaumann.bookastylist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class CustomerAlreadyBookedException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Customer Already booked on that slot";
    }
}
