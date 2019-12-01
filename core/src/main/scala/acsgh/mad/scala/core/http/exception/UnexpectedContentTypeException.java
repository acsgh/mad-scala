package acsgh.mad.scala.core.http.exception;

public class UnexpectedContentTypeException extends IllegalArgumentException {
    public UnexpectedContentTypeException(String contentType) {
        super("Unexpected content type '" + contentType + "'");
    }
}
