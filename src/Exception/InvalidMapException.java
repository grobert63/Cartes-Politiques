package exception;

/**
 * Created by Guillaume Robert on 13/11/2015.
 */
public class InvalidMapException extends Exception {
    public InvalidMapException() {
    }

    public InvalidMapException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMapException(String message) {
        super(message);
    }

    public InvalidMapException(Throwable cause) {
        super(cause);
    }
}
