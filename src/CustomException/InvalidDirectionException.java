package CustomException;

/**
 * Erreur lancée si la direction est invalide
 */
public class InvalidDirectionException extends Exception {

    public InvalidDirectionException() {
        super("Une direction doit être comprise entre 0 et 5");
    }

}
