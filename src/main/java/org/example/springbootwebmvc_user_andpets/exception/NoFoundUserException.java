package org.example.springbootwebmvc_user_andpets.exception;

public class NoFoundUserException extends RuntimeException {
    public NoFoundUserException(String message) {
        super(message);
    }
}
