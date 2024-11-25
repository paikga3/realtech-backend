package realtech.api.common.exception;

/**
 * JWT가 만료된 경우를 처리하기 위한 예외
 * 
 * @author paikjonghyun
 *
 */
public class JwtExpiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JwtExpiredException(String message) {
        super(message);
    }
}
