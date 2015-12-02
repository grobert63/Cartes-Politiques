package Entities;

import java.util.List;

/**
 * Classe permettant la gestion de toutes les données relatives à la carte non traitée
 * /!\ Nom de classe à modifier pour ne pas confondre avec la classe java.util.Map; /!\
 */
public class Map {
    private final double _width;
    private final double _height;
    private List<Region> _regions;

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
        for (Region region : _regions) {
            region.setMainCenter(1, height, width);
        }
    }

    /**
     * Constructeur complet
     * @param width Largeur totale de la carte (X)
     * @param height Longueur totale de la carte (Y)
     * @param _regions Liste des régions de la carte
     * @param acceptedPercentForPolygonsOfSameRegion Le pourcentage de distance relative à la taille de la carte pour considerer deux polygones comme se touchant
     */
    public Map(double width, double height, List<Region> _regions, double acceptedPercentForPolygonsOfSameRegion) {
        this._width = width;
        this._height = height;
        this._regions = _regions;
        for (Region region : _regions) {
            region.setMainCenter(acceptedPercentForPolygonsOfSameRegion, height, width);
        }
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
