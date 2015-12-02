package Entities;

import Loader.PolygonInfo;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Décrit une région
 * @author Théophile
 */
public class Region {
    private HashMap<String,String> data = new HashMap<>();
    private String _defaultField = null;
    private Point _center;
    private Point _mainCenter;
    private List<Polygon> _borders = new ArrayList<>();
    private Polygon _mainBorder;

    /**
     * Construit une région en précisant son centre de gravité
     * @param center Centre de gravité de la région
     * @param borders Liste de Polygones JavaFX representant les frontières de la région
     */
    public Region(Point center, List<Polygon> borders) {
        this._center = center;
        this._borders = borders;
        _mainBorder = new PolygonInfo(_borders).getMainPolygon();
    }
    
    /**
     * Construit une région à partir de ses frontières
     * @param borders Liste de Polygones JavaFX representant les frontières de la région
     */
    public Region(List<Polygon> borders) {
        setBorders(borders);
        _mainBorder = new PolygonInfo(_borders).getMainPolygon();
    }

    /**
     * Retourne le centre de gravité principal
     * @return Centre de gravité principal
     */
    public Point getMainCenter(){
        return _mainCenter;
    }

    /**
     * Retourne le Polygone JavaFX des frontières de la région
     * @return Polygone des frontières
     */
    public List<Polygon> getBorders() {
        return _borders;
    }

    /**
     * Modifie la frontière d'une région
     * @param borders Polygone JavaFX contenant les nouvelles frontières
     */
    public void setBorders(List<Polygon> borders) {
        if (borders != null)
        {
            this._center = new PolygonInfo(borders).getCentreDeMasse();
        }
        else
        {
            this._center = Point.Zero;
        }
        this._borders = borders;
    }

    /**
     * Renseigne le centre de gravité principal de la région
     * @param acceptedPercent Le pourcentage de distance relative à la taille de la carte pour considerer deux polygones comme se touchant
     * @param mapHeight Hauteur de la carte
     * @param mapWidth Largeur de la carte
     */
    public void setMainCenter(double acceptedPercent, double mapHeight, double mapWidth) {
        Point2D mainCenter = new PolygonInfo(_borders).getCentreDeMassePrincipal(acceptedPercent, mapHeight, mapWidth);
        this._mainCenter = new Point(mainCenter.getX(),mainCenter.getY());
    }

    /**
     * Renseigne une donnée sur cette région.
     * (Cette donnée provient généralement du fichier .dbf)
     * @param field Champ / Nom de la colonne ("nom_region", "nb_habitants", ...)
     * @param value Valeur de la donnée ("Auvergne", "1 300 000", ...)
     */
    public void setData(String field, String value){
        data.put(field, value);
    }
    
    /**
     * Récupère la donnée de la région lié au champ renseigné
     * @param field Champ / Colonne
     * @return Donnée liée au champ renseigné
     */
    public String getData(String field){
        return data.get(field);
    }
    
    /**
     * Récupère le nom du champ lié au nom de la région.
     * Ce champ est nommé "champ par défaut".
     * @return Champ par défaut
     */
    public String getDefaultField() {
        return _defaultField;
    }

    /**
     * Renseigne quel champ doit contenir le nom de la région.
     * Ce champ est nommé "champ par défaut".
     * @param defaultField Champ par défaut
     */
    public void setDefaultField(String defaultField) {
        if(data.containsKey(defaultField)) {
            this._defaultField = defaultField;
        }
    }

    /**
     * @return Le centre de gravité de la région
     */
    public Point getCenter(){
        return _center;
    }

    
    /**
     * Indique le nom de la région, soit la valeur lié au champ par défaut.
     * Le champ par défaut doit avoir été au préalablement renseigné par un appel à la méthode setDefaultField.
     * @return Nom de la région, ou "noname" s'il n'y a pas de champ par défaut.
     */
    public String getName(){
        if(this._defaultField != null) {
            return data.get(_defaultField);
        }
        return "noname";
    }
    
    /**
     * Distance du centre de gravité d'une région à une autre
     * @param other Autre région
     * @return Distance entre les deux centres de gravité
     */
    public double getDistanceTo(Region other){
        //return Math.sqrt(Math.pow(this.getCenterX() - other.getCenterX(), 2) + Math.pow(this.getCenterY() - other.getCenterY(), 2));
        return Math.sqrt(Math.pow(this.getMainCenter().x - other.getMainCenter().x, 2) + Math.pow(this.getMainCenter().y - other.getMainCenter().y, 2));
    }

    public double getAngleTo(Region other){
        double vecteurX = other.getMainCenter().x - this.getMainCenter().x;
        double vecteurY = other.getMainCenter().y - this.getMainCenter().y;
        return (Math.atan2(vecteurY, vecteurX) * -(180/Math.PI) + 450) % 360;
    }
    
    public Polygon getMainBorder(){
        return _mainBorder;
    }
}
