package Entities;

import Debug.TimeDebug;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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
        
        TimeDebug.timeStart(20);
        calculateNeighbors(_regions, _bm.getBoundaries());
        TimeDebug.timeStop(20);
    }
    
    public List<Region> getRegions(){
        return _regions;
    }
    
    public List<Boundary> getBoundaries(){
        return _bm.getBoundaries();
    }
    
    private void calculateNeighbors(List<Region> regions, List<Boundary> allBoundaries){
        List<Region> involvedRegions = new LinkedList<>();
        BoundPolygon currentPoly;
        
        for(Boundary b : allBoundaries){
            involvedRegions.clear();
            
            for(Region r : regions){
                currentPoly = r.getBoundMainPolygon();
                if(currentPoly.getBoundaries().contains(b)){
                    involvedRegions.add(r);
                }
            }
            
            Iterator<Region> it = involvedRegions.iterator();
            switch(involvedRegions.size()){
                case 1 :
                    it.next().addNeighbor(null, b);
                    break;
                case 2 :
                    Region r1 = it.next(), r2 = it.next();
                    r1.addNeighbor(r2, b);
                    r2.addNeighbor(r1, b);
                    break;
                default :
                    System.err.println("involvedRegions.size() = "+involvedRegions.size());
                    break;
            }
        }
    }

    public void setRegionsName(String defaultField){
        for(Region r : _regions){
            r.setDefaultField(defaultField);
        }
    }
}
