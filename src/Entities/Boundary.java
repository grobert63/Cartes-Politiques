package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Décrit une frontière
 * @author Théophile
 */
public class Boundary {
    private final List<Point> _points;
    
    public Boundary(){
        _points = new ArrayList<>();
    }
    
    /**
     * Construire une frontière contenant un ensemble de points
     * @param points Points contenus par la frontière
     */
    public Boundary(List<Point> points){
        _points = points;
    }
    
    /**
     * Retourne tous les points d'une frontière
     * @return Points contenus par la frontière
     */
    public List<Point> getPoints(){
        return _points;
    }
    
    /**
     * Retourne le premier point d'une frontière
     * @return Le premier point d'une frontière
     */
    public Point getStartingPoint(){
        return _points.get(0);
    }
    
    /**
     * Retourne le dernier point d'une frontière
     * @return Le dernier point d'une frontière
     */
    public Point getEndingPoint(){
        return _points.get(_points.size()-1);
    }
    
    /**
     * Retourne la longueur d'une frontière c-a-d la somme des distances entre chaque point
     * @return Longueur d'une frontière
     */
    public double getLength(){
        double length = 0;
        
        Point previous;
        Point current = _points.get(0);
        for(Point p : _points){
            previous = current;
            current = p;
            length += Geometry.distanceBetween2Points(previous, current);
        }
        
        return length;
    }
}
