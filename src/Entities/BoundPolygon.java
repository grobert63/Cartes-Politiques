package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Théophile
 */
public class BoundPolygon {
    private final List<Boundary> _boundaries = new ArrayList<>();

    public List<Boundary> getBoundaries(){
        return _boundaries;
    }
    
    public double getPerimeter(){
        double perimeter = 0;
        for(Boundary b : _boundaries){
            perimeter += b.getLength();
        }
        return perimeter;
    }
}