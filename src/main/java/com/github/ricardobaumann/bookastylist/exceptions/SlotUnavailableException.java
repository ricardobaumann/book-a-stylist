package com.github.ricardobaumann.bookastylist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class SlotUnavailableException extends RuntimeException {
    @Override
    public String getMessage() {
        return "There are no stylists available on that slot";
    }
}
