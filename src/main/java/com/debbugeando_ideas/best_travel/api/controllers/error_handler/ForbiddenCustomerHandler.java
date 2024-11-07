package com.debbugeando_ideas.best_travel.api.controllers.error_handler;

import com.debbugeando_ideas.best_travel.api.models.responses.BaseErrorResponse;
import com.debbugeando_ideas.best_travel.api.models.responses.ErrorResponse;
import com.debbugeando_ideas.best_travel.util.exceptions.ForbiddenCustomerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenCustomerHandler {

    @ExceptionHandler(ForbiddenCustomerException.class)
    public BaseErrorResponse handleForbiddenCustomer (ForbiddenCustomerException exception){
        return ErrorResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.FORBIDDEN.name())
                .errorCode(HttpStatus.FORBIDDEN.value())
                .build();
    }
}
