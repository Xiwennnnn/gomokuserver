package whpu.gomoku.gomokuserver.exception.authen;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super();
    }
    public TokenExpiredException(String message) {
        super(message);
    }
    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
