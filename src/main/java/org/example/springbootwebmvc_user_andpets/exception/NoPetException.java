package org.example.springbootwebmvc_user_andpets.exception;

public class NoPetException extends RuntimeException {
    public NoPetException(String message) {
        super(message);
    }
}
