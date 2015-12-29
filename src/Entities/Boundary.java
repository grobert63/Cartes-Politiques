package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Théophile
 */
public class Boundary {
    private final List<Point> _points;
    
    public Boundary(){
        _points = new ArrayList<>();
    }
    
    public Boundary(List<Point> points){
        _points = points;
    }
    
    public List<Point> getPoints(){
        return _points;
    }
    
    public Point getStartingPoint(){
        return _points.get(0);
    }
    
    public Point getEndingPoint(){
        return _points.get(_points.size()-1);
    }
    
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
