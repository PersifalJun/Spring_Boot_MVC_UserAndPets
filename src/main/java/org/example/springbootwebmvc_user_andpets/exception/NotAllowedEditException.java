package org.example.springbootwebmvc_user_andpets.exception;

public class NotAllowedEditException extends RuntimeException {
    public NotAllowedEditException(String message) {
        super(message);
    }
}
