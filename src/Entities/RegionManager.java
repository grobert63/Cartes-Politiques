package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Théophile
 */
public class RegionManager {
    private final BoundaryManager _bm;
    private final List<Region> _regions = new ArrayList<>();

    public RegionManager(List<List<RawPolygon>> rawRegions){
        int nbRegions = rawRegions.size();
        
        RawPolygon[] rawMainPolygons = new RawPolygon[nbRegions];
        List<RawPolygon>[] displayablePolygons = new ArrayList[nbRegions];
        for(int i=0 ; i<nbRegions ; i++){
            List<RawPolygon> polygons = rawRegions.get(i);
            displayablePolygons[i] = polygons;
            rawMainPolygons[i] = Geometry.getMainPolygon(polygons);
        }
        
        _bm = new BoundaryManager(rawMainPolygons);
        BoundPolygon[] boundMainPolygons = _bm.getBoundPolygon();
        
        for(int i=0 ; i<nbRegions ; i++){
            Region r = new Region(displayablePolygons[i],rawMainPolygons[i],boundMainPolygons[i]);
            _regions.add(r);
        }
    }
    
    public List<Region> getRegions(){
        return _regions;
    }
    
    public List<Boundary> getBoundaries(){
        return _bm.getBoundaries();
    }
}
