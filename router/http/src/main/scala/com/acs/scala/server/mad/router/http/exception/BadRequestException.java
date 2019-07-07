package com.acs.scala.server.mad.router.http.exception;

public class BadRequestException extends IllegalArgumentException {
    public BadRequestException(String s) {
        super(s);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }
}
