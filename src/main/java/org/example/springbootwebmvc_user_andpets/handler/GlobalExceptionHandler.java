package org.example.springbootwebmvc_user_andpets.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootwebmvc_user_andpets.errorDto.ServerErrorDto;
import org.example.springbootwebmvc_user_andpets.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerErrorDto> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Got validation exception", ex);

        String detailedMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> (error.getField() + ": " + error.getDefaultMessage()))
                .collect(Collectors.joining(","));
        var errorDto = getErrorDto("Validation Error",
                detailedMessage);

        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(NotAllowedEditException.class)
    public ResponseEntity<ServerErrorDto> handleEditingException(NotAllowedEditException ex) {
        log.warn("Got editing exception", ex);
        var errorDto = getErrorDto("User editing error",
                ex.getMessage());
        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(errorDto);

    }

    @ExceptionHandler(AlreadyRegisteredException.class)
    public ResponseEntity<ServerErrorDto> handleRegistryException(AlreadyRegisteredException ex) {
        log.warn("Got registry exception", ex);
        var errorDto = getErrorDto("User registry error",
                ex.getMessage());
        return ResponseEntity.
                status(HttpStatus.CONFLICT)
                .body(errorDto);

    }

    @ExceptionHandler(NoFoundUserException.class)
    public ResponseEntity<ServerErrorDto> handleNoUserException(NoFoundUserException ex) {
        log.warn("Got NoUserException", ex);
        var errorDto = getErrorDto("User search error",
                ex.getMessage());
        return ResponseEntity.
                status(HttpStatus.NOT_FOUND)
                .body(errorDto);

    }

    @ExceptionHandler(NoFoundPetException.class)
    public ResponseEntity<ServerErrorDto> handleNoPetException(NoFoundPetException ex) {
        log.warn("Got NoPetException", ex);
        var errorDto = getErrorDto("Pet search error",
                ex.getMessage());
        return ResponseEntity.
                status(HttpStatus.NOT_FOUND)
                .body(errorDto);

    }

    @ExceptionHandler(InvalidPetException.class)
    public ResponseEntity<ServerErrorDto> handleInvalidPetException(InvalidPetException ex) {
        log.warn("Got InvalidPetException", ex);
        var errorDto = getErrorDto("Null fields found",
                ex.getMessage());

        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ServerErrorDto> handleNumberFormatException(NumberFormatException ex) {
        log.warn("Got NumberFormatException", ex);

        var errorDto = getErrorDto("Invalid request params format"
                , ex.getMessage());

        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(NoFoundOwnerPetException.class)
    public ResponseEntity<ServerErrorDto> handleNumberFormatException(NoFoundOwnerPetException ex) {
        log.warn("Got NoFoundOwnerPetException", ex);

        var errorDto = getErrorDto("Not possible to find owner`s pet"
                , ex.getMessage());

        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerErrorDto> handleNumberFormatException(Exception ex) {
        log.warn("Got other exception", ex);

        var errorDto = getErrorDto("Exception"
                , ex.getMessage());

        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    private ServerErrorDto getErrorDto(
            String message,
            String detailedMessage
    ) {
        return new ServerErrorDto(
                message,
                detailedMessage,
                LocalDateTime.now()
        );
    }
}
