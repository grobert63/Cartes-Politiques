package Entities;

import java.util.List;

/**
 * Classe permettant la gestion de toutes les données relatives à la carte non traitée
 * /!\ Nom de classe à modifier pour ne pas confondre avec la classe java.util.Map; /!\
 */
public class Map {
    private final double _width;
    private final double _height;
    private final List<Region> _regions;

    /**
     * Constructeur complet
     * @param width Largeur totale de la carte (X)
     * @param height Longueur totale de la carte (Y)
     * @param _regions Liste des régions de la carte
     */
    public Map(double width, double height, List<Region> _regions) {
        this._width = width;
        this._height = height;
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
