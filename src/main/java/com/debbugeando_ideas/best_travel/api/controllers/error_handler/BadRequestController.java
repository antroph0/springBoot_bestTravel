package com.debbugeando_ideas.best_travel.api.controllers.error_handler;

import com.debbugeando_ideas.best_travel.api.models.responses.BaseErrorResponse;
import com.debbugeando_ideas.best_travel.api.models.responses.ErrorResponse;
import com.debbugeando_ideas.best_travel.api.models.responses.ErrorsResponse;
import com.debbugeando_ideas.best_travel.util.exceptions.IdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Array;
import java.util.ArrayList;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestController {

    @ExceptionHandler(IdNotFoundException.class)
    public BaseErrorResponse handlerIdNotFound(IdNotFoundException e){
        return ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.name())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseErrorResponse handlerIdNotFound(MethodArgumentNotValidException e){
        var errors = new ArrayList<String>();
        e.getAllErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));

        return ErrorsResponse.builder()
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST.name())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .build();

    }
}
