package CustomException;

/**
 * erreur lancee si la carte n'est pas dans un format lisble par le logiciel
 */
public class InvalidMapException extends Exception {
    public InvalidMapException() {
        super();
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
