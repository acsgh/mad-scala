package acsgh.mad.scala.router.http.exception;

public class UnexpectedContentTypeException extends IllegalArgumentException {
    public UnexpectedContentTypeException(String contentType) {
        super("Unexpected content type '" + contentType + "'");
    }
}
