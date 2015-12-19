package Entities;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Décrit une région
 * @author Théophile
 */
public class Region {
    private Map<String,String> infos = new HashMap<>();
    private String _defaultField = null;
    
    private List<RawPolygon> _displayablePolygons;
    private RawPolygon _rawMainPolygon;
    private BoundPolygon _boundMainPolygon;
    
    private Point _center;

    
    // Constructeur à modifier
    public Region(List<RawPolygon> displayablePolygons, RawPolygon rawMainPolygon, BoundPolygon boundMainPolygon) {
        _displayablePolygons = displayablePolygons;
        _rawMainPolygon = rawMainPolygon;
        _boundMainPolygon = boundMainPolygon;
        
        _center = Geometry.getCentreDeMasse(rawMainPolygon);
    }

    public List<RawPolygon> getDisplayablePolygons() {
        return _displayablePolygons;
    }
    
    public RawPolygon getRawMainPolygon(){
        return _rawMainPolygon;
    }
    
    public BoundPolygon getBoundMainPolygon(){
        return _boundMainPolygon;
    }
    
    public Point getCenter(){
        return _center;
    }
    
    /**
     * Renseigne une donnée sur cette région.
     * (Cette donnée provient généralement du fichier .dbf)
     * @param field Champ / Nom de la colonne (ex : "nom_region" ou "nb_habitants"...)
     * @param value Valeur de la donnée (ex : "Auvergne" ou "1 300 000"...)
     */
    public void setInfo(String field, String value){
        infos.put(field, value);
    }
    
    /**
     * Récupère la donnée de la région lié au champ renseigné
     * @param field Champ / Colonne
     * @return Donnée liée au champ renseigné
     */
    public String getInfo(String field){
        return infos.get(field);
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
        if(infos.containsKey(defaultField)) {
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
            return infos.get(_defaultField);
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
}
