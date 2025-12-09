package org.example.springbootwebmvc_user_andpets.exception;

public class AlreadyRegisteredException extends RuntimeException {
    public AlreadyRegisteredException(String message) {
        super(message);
    }
}
