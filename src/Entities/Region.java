package Entities;

import Loader.PolygonInfo;
import javafx.scene.shape.Polygon;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;

import java.util.HashMap;

/**
 * Décrit une région
 * @author Théophile
 */
public class Region {
    private HashMap<String,String> data = new HashMap<>();
    private String _defaultField = null;
    private double _centerX;
    private double _centerY;
    private Polygon _border;

    /**
     * Construit une région en précisant son centre de gravité
     * @param centerX Position en X du centre de gravité de la région
     * @param centerY Position en Y du centre de gravité de la région
     * @param border Polygone JavaFX representant les frontières de la région
     */
    public Region(double centerX, double centerY, Polygon border) {
        this._centerX = centerX;
        this._centerY = centerY;
        this._border = border;
    }

    /**
     * Construit une région à partir de ses frontières
     * @param border Polygone JavaFX representant les frontières de la région
     */
    public Region(Polygon border) {
        setBorder(border);
    }

    /**
     * Retourne le Polygone JavaFX des frontières de la région
     * @return Polygone des frontières
     */
    public Polygon getBorder() {
        return _border;
    }

    /**
     * Modifie la frontière d'une région
     * @param border Polygone JavaFX contenant les nouvelles frontières
     */
    public void setBorder(Polygon border) {
        if (border != null)
        {
            PointData centreDeMasse = new PolygonInfo(border).getCentreDeMasse();
            this._centerX = centreDeMasse.getX();
            this._centerY = centreDeMasse.getY();
        }
        else
        {
            this._centerX = 0;
            this._centerY = 0;
        }
        this._border = border;
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
     * @return La position en X du centre de gravité de la région
     */
    public double getCenterX() {
        return _centerX;
    }

    /**
     * @return La position en Y du centre de gravité de la région
     */
    public double getCenterY() {
        return _centerY;
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
        return Math.sqrt(Math.pow(this.getCenterX() - other.getCenterX(), 2) + Math.pow(this.getCenterY() - other.getCenterY(), 2));
    }

    public double getAngleTo(Region other){
        double vecteurX = other.getCenterX() - this.getCenterX();
        double vecteurY = other.getCenterY() - this.getCenterY();
        return (Math.atan2(vecteurY, vecteurX) * -(180/Math.PI) + 450) % 360;
    }
}
