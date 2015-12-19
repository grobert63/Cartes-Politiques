package Entities;

import java.util.List;

/**
 * Classe permettant la gestion de toutes les données relatives à la carte non traitée
 * /!\ Nom de classe à modifier pour ne pas confondre avec la classe java.util.Map; /!\
 */
public class Map {
    private final double _width;
    private final double _height;
    private final List<Boundary> _boundaries;
    private final List<Boundary> _simpleBoundaries;
    private final List<Region> _regions;

    // à arranger
    private static final double COEF_SIMPLIFY = 0.3;
    
    /**
     * Constructeur complet
     * @param width Largeur totale de la carte (X)
     * @param height Longueur totale de la carte (Y)
     * @param rm
     */
    public Map(double width, double height, RegionManager rm) {
        this._width = width;
        this._height = height;
        this._regions = rm.getRegions();
        this._boundaries = rm.getBoundaries();
        this._simpleBoundaries = Geometry.getSimplifyBoundaries(_boundaries, COEF_SIMPLIFY);
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
    
    public List<Boundary> getBoundaries(){
        return _boundaries;
    }
    
    public List<Boundary> getSimpleBoundaries(){
        return _simpleBoundaries;
    }
}
