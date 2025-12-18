package kr.java.restapi.exception;

// #(3)-2
/**
 * 404 Not Found 예외
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException forItem(Long id) {
        return new NotFoundException("상품을 찾을 수 없습니다. ID: " + id);
    }

    public static NotFoundException forFile(Long id) {
        return new NotFoundException("파일을 찾을 수 없습니다. ID: " + id);
    }
}