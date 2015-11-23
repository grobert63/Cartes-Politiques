package Entities;

import java.util.List;

/**
 * Classe permettant la gestion de toutes les données relatives à la carte non traitée
 */
public class Map {
    private final double _width;
    private final double _height;
    private List<Region> _regions;

    /**
     * Constructeur complet
     * @param _width Largeur totale de la carte (X)
     * @param _height Longueur totale de la carte (Y)
     * @param _regions Liste des régions de la carte
     */
    public Map(double _width, double _height, List<Region> _regions) {
        this._width = _width;
        this._height = _height;
        this._regions = _regions;
    }

    /**
     * Retourne la largeur de la carte
     * @return Largeur de la carte
     */
    public double getWidth() {
        return _width;
    }

    /**
     * Retourne la longueur de la carte
     * @return Longueur de la carte
     */
    public double getHeight() {
        return _height;
    }

    /**
     * Retourne la liste des régions de la carte
     * @return Liste des régions
     */
    public List<Region> getRegions() {
        return _regions;
    }
}
