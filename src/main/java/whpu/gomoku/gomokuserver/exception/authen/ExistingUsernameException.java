package whpu.gomoku.gomokuserver.exception.authen;

public class ExistingUsernameException extends RuntimeException {
    public ExistingUsernameException() {
        super();
    }
    public ExistingUsernameException(String message) {
        super(message);
    }
    public ExistingUsernameException(String message, Throwable cause) {
        super(message, cause);
    }
}
