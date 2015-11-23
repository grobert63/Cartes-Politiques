package exception;

/**
 * Erreur lancée si la direction est invalide
 */
public class InvalidDirectionException extends Exception {
    public InvalidDirectionException() {
        super();
    }

    public InvalidDirectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDirectionException(String message) {
        super(message);
    }

    public InvalidDirectionException(Throwable cause) {
        super(cause);
    }
}
