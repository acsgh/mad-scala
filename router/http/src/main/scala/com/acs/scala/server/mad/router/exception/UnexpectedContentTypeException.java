package com.acs.scala.server.mad.router.exception;

public class UnexpectedContentTypeException extends IllegalArgumentException{
    public UnexpectedContentTypeException(String contentType) {
        super("Unexpected content type '" + contentType + "'");
    }
}
