package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Théophile
 */
public class RawPolygon {
    private List<Point> _points = new ArrayList<>();

    public List<Point> getPoints() {
        return _points;
    }

    public boolean contains(Point p) {
        return _points.contains(p);
    }
}
