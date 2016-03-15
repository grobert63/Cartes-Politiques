package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Décrit un polygone contenant un ensemble de frontière (Boundary)
 *
 * @author Théophile
 */
public class BoundPolygon {
    private final List<Boundary> _boundaries = new ArrayList<>();

    /**
     * Retourne une liste des frontières contenues par ce polygone
     *
     * @return Liste de frontières
     */
    public List<Boundary> getBoundaries() {
        return _boundaries;
    }

}