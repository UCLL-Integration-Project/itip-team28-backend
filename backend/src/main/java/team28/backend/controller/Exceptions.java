package team28.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import team28.backend.exceptions.UserException;

import java.util.Map;

@RestControllerAdvice
public class Exceptions {
    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserException(UserException UserException) {
        return Map.of(
                "status", "Service error",
                "message", UserException.getMessage());
    }

}