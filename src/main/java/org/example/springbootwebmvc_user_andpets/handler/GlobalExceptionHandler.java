package org.example.springbootwebmvc_user_andpets.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootwebmvc_user_andpets.errorDto.ServerErrorDto;
import org.example.springbootwebmvc_user_andpets.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerErrorDto> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Got validation exception", ex);

        String detailedMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> (error.getField() + ": " + error.getDefaultMessage()))
                .collect(Collectors.joining(","));

        var errorDto = new ServerErrorDto(
                "Validation Error",
                detailedMessage,
                LocalDateTime.now()
        );
        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(NotAllowedEditException.class)
    public ResponseEntity<ServerErrorDto> handleEditingException(NotAllowedEditException ex) {
        log.error("Got editing exception", ex);
        var errorDto = new ServerErrorDto(
                "User editing error",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(errorDto);

    }

    @ExceptionHandler(RegistryException.class)
    public ResponseEntity<ServerErrorDto> handleRegistryException(RegistryException ex) {
        log.error("Got registry exception", ex);
        var errorDto = new ServerErrorDto(
                "User registry  error",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.
                status(HttpStatus.CONFLICT)
                .body(errorDto);

    }

    @ExceptionHandler(NoUserException.class)
    public ResponseEntity<ServerErrorDto> handleNoUserException(NoUserException ex) {
        log.error("Got NoUserException", ex);
        var errorDto = new ServerErrorDto(
                "User search error",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.
                status(HttpStatus.NOT_FOUND)
                .body(errorDto);

    }

    @ExceptionHandler(NoPetException.class)
    public ResponseEntity<ServerErrorDto> handleNoPetException(NoPetException ex) {
        log.error("Got NoPetException", ex);
        var errorDto = new ServerErrorDto(
                "Pet search error",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.
                status(HttpStatus.NOT_FOUND)
                .body(errorDto);

    }

    @ExceptionHandler(InvalidPetException.class)
    public ResponseEntity<ServerErrorDto> handleInvalidPetException(InvalidPetException ex) {
        log.error("Got InvalidPetException", ex);
        var errorDto = new ServerErrorDto(
                "Null fields found",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ServerErrorDto> handleNumberFormatException(NumberFormatException ex) {
        log.error("Got NumberFormatException", ex);
        var errorDto = new ServerErrorDto(
                "Invalid request params format",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

}
