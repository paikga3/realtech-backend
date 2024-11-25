package realtech.api.common.exception;

/**
 * JWT가 유효하지 않은 경우를 처리하기 위한 예외
 * 
 * @author paikjonghyun
 *
 */
public class InvalidJwtException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidJwtException(String message) {
        super(message);
    }
}
