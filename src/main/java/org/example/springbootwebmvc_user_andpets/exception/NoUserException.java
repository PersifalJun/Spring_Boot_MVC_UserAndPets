package org.example.springbootwebmvc_user_andpets.exception;

public class NoUserException extends RuntimeException {
    public NoUserException(String message) {
        super(message);
    }
}
