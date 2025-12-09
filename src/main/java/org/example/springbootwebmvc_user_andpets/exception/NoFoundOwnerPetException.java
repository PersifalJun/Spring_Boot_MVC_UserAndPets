package org.example.springbootwebmvc_user_andpets.exception;

public class NoFoundOwnerPetException extends RuntimeException {
    public NoFoundOwnerPetException(String message) {
        super(message);
    }
}
