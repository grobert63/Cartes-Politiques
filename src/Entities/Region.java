package Entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

/**
 * Décrit une région
 * @author Théophile
 */
public class Region {
    private final Map<String,String> infos = new HashMap<>();
    private String _defaultField = null;
    
    private final List<RawPolygon> _displayablePolygons;
    private final RawPolygon _rawMainPolygon;
    private final BoundPolygon _boundMainPolygon;
    
    private final Point _center;
    
    // equals et hashcode peut-être à redéfinir 
    private final Map<Region,List<Boundary>> _neighbors = new HashMap<>();

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
        return Geometry.angleBetween2Points(this.getCenter(),other.getCenter());
    }
    
    void addNeighbor(Region neighbor, Boundary commonBoundary){
        if(!_neighbors.containsKey(neighbor)){
            _neighbors.put(neighbor, new LinkedList<>());
        }
        _neighbors.get(neighbor).add(commonBoundary);
    }

    public Map<Region, List<Boundary>> getNeighbors() {
        return _neighbors;
    }
}
