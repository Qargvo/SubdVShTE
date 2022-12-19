package com.study.subdvshte.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerException {
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "")
    @ExceptionHandler(NotFoundException.class)
    public void handlerNotFoundException(){}
}
