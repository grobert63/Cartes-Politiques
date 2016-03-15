package Entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Décrit une région
 *
 * @author Théophile
 */
public class Region {
    private final Map<String, String> infos = new HashMap<>();
    private final BoundPolygon _boundMainPolygon;
    private final Point _center;
    // equals et hashcode peut-être à redéfinir
    private final Map<Region, List<Boundary>> _neighbors = new HashMap<>();
    private String _defaultField = null;

    // Constructeur à modifier
    public Region(RawPolygon rawMainPolygon, BoundPolygon boundMainPolygon) {
        _boundMainPolygon = boundMainPolygon;
        _center = Geometry.getCentreDeMasse(rawMainPolygon);
    }

    /**
     * Obtient le BoundPolygon (contenant les Boundaries) de la région
     *
     * @return Le BoundPolygon de la région
     */
    public BoundPolygon getBoundMainPolygon() {
        return _boundMainPolygon;
    }

    /**
     * Obtient le centre de masse de la région
     *
     * @return Centre de masse de la région
     */
    public Point getCenter() {
        return _center;
    }

    /**
     * Renseigne une donnée sur cette région.
     * (Cette donnée provient généralement du fichier .dbf)
     *
     * @param field Champ / Nom de la colonne (ex : "nom_region" ou "nb_habitants"...)
     * @param value Valeur de la donnée (ex : "Auvergne" ou "1 300 000"...)
     */
    public void setInfo(String field, String value) {
        infos.put(field, value);
    }

    /**
     * Renseigne quel champ doit contenir le nom de la région.
     * Ce champ est nommé "champ par défaut".
     *
     * @param defaultField Champ par défaut
     */
    public void setDefaultField(String defaultField) {
        if (infos.containsKey(defaultField)) {
            this._defaultField = defaultField;
        }
    }

    /**
     * Indique le nom de la région, soit la valeur lié au champ par défaut.
     * Le champ par défaut doit avoir été au préalablement renseigné par un appel à la méthode setDefaultField.
     *
     * @return Nom de la région, ou "noname" s'il n'y a pas de champ par défaut.
     */
    public String getName() {
        if (this._defaultField != null) {
            return infos.get(_defaultField);
        }
        return "";
    }

    /**
     * Distance du centre de gravité d'une région à une autre
     *
     * @param other Autre région
     * @return Distance entre les deux centres de gravité
     */
    public double getDistanceTo(Region other) {
        return Math.sqrt(Math.pow(this.getCenter().x - other.getCenter().x, 2) + Math.pow(this.getCenter().y - other.getCenter().y, 2));
    }


    /**
     * Angle entre le centre de masse de la région "other" et celui de cette région.
     * Ex 1 : Si le centre de "other" est au-dessus du centre de cette région, l'angle sera 0°
     * Ex 2 : Si le centre de "other" est à droite du centre de cette région, l'angle sera 90°
     *
     * @param other Autre région
     * @return Angle entre les centres des 2 régions
     */
    public double getAngleTo(Region other) {
        return Geometry.angleBetween2Points(this.getCenter(), other.getCenter());
    }

    /**
     * Ajoute un voisin à cette région en spécifiant la frontière commune.
     * Si la région "neighbor" a déjà été spécifiée comme voisine de celle-ci,
     * la frontière "commonBoundary" est ajoutée à la liste des frontières communes entre ces deux régions.
     * L'utilisation de cette méthode est réservée à la classe RegionManager
     *
     * @param neighbor       Région voisine/adjacente à celle-ci
     * @param commonBoundary Frontière commune
     */
    void addNeighbor(Region neighbor, Boundary commonBoundary) {
        if (!_neighbors.containsKey(neighbor)) {
            _neighbors.put(neighbor, new LinkedList<>());
        }
        _neighbors.get(neighbor).add(commonBoundary);
    }

    public boolean IsCommunBoundary(Region r) {
        for (Boundary b : getBoundMainPolygon().getBoundaries()) {
            for (Boundary b2 : r.getBoundMainPolygon().getBoundaries()) {
                if (b == b2) return true;
            }
        }
        return false;
    }
}
