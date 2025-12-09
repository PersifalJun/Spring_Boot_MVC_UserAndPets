package org.example.springbootwebmvc_user_andpets.exception;

public class NoFoundPetException extends RuntimeException {
    public NoFoundPetException(String message) {
        super(message);
    }
}
