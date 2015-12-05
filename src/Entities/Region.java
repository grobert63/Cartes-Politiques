package Entities;

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
    private List<Polygon> _boundaries = new ArrayList<>();
    private Polygon _mainPolygon;
    
    /**
     * Construit une région à partir de ses frontières
     * @param boundaries Liste de Polygones JavaFX representant les frontières de la région
     */
    public Region(List<Polygon> boundaries) {
        _boundaries = boundaries;
        _mainPolygon = Geometry.getMainPolygon(_boundaries);
        _center = Geometry.getCentreDeMasse(_mainPolygon);
    }

    public Point getCenter(){
        return _center;
    }

    public List<Polygon> getBoundaries() {
        return _boundaries;
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
        return Math.sqrt(Math.pow(this.getCenter().x - other.getCenter().x, 2) + Math.pow(this.getCenter().y - other.getCenter().y, 2));
    }

    public double getAngleTo(Region other){
        double vecteurX = other.getCenter().x - this.getCenter().x;
        double vecteurY = other.getCenter().y - this.getCenter().y;
        return (Math.atan2(vecteurY, vecteurX) * -(180/Math.PI) + 450) % 360;
    }
    
    public Polygon getMainPolygon(){
        return _mainPolygon;
    }
    
    public double pourcentageDeFrontiereCommune(Region other){
        return Geometry.ratioCommonBoudaries(this.getMainPolygon(), other.getMainPolygon());
    }
}
