package Entities;

import java.util.List;

/**
 * Created by Guillaume Robert on 23/11/2015.
 */
public class Map {
    private final double _width;
    private final double _height;
    private List<Region> _regions;

    public Map(double _width, double _height, List<Region> _regions) {
        this._width = _width;
        this._height = _height;
        this._regions = _regions;
    }

    public double getWidth() {
        return _width;
    }

    public double getHeight() {
        return _height;
    }

    public List<Region> getRegions() {
        return _regions;
    }
}
