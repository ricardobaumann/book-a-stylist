package com.github.ricardobaumann.bookastylist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class PastDateAppointmentException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Appointments cannot be created in the past";
    }
}
