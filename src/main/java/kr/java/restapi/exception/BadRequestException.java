package kr.java.restapi.exception;

// (3)-3
/**
 * 400 Bad Request 예외
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
