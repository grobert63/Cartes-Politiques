package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Théophile
 */
public class RawPolygon {
    private List<Point> _points = new ArrayList<>();

    public List<Point> getPoints(){
        return _points;
    }
    /*
    public void add(Point p){
        _points.add(p);
    }
    */
    public boolean contains(Point p){
        return _points.contains(p);
    }
    
    /*
    public Point get(int index){
        return _points.get(index);
    }
    
    public int size(){
        return _points.size();
    }
    */
}
