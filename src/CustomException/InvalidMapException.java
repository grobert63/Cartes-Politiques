package CustomException;

/**
 * erreur lancee si la carte n'est pas dans un format lisble par le logiciel
 */
public class InvalidMapException extends Exception {

    public InvalidMapException(String message) {
        super(message);
    }

}
