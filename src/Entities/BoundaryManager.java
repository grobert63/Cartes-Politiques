package Entities;

import Debug.TimeDebug;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 *
 * @author Théophile
 */
public class BoundaryManager {
    private final List<Boundary> _boundaries = new ArrayList<>();
    private final PointSeeker _seeker;
    private final BoundPolygon[] _boundPolygons;
    
    public BoundaryManager(RawPolygon[] raws){
        TimeDebug.timeStart(2);
        
        _seeker = new PointSeeker(raws);
        
        TimeDebug.timeStop(2);
        
        BoundPolygon[] bounds = new BoundPolygon[raws.length];
        for(int i=0 ; i<bounds.length ; i++){
            bounds[i] = new BoundPolygon();
        }
        
        Map<Integer,List<Boundary>> boundariesToBeAdd = new HashMap<>();
        
        
        for(int i=0 ; i<raws.length ; i++){
            
            if(bounds[i].getBoundaries().isEmpty()){
                TimeDebug.timeStart(5);
                makeBoundaries(raws,i,boundariesToBeAdd);
                TimeDebug.timeStop(5);
                
            }
            else{
                TimeDebug.timeStart(6);

                List<int[]> extremities = getExtremitiesOfBoundariesToBeMade(bounds[i],raws[i]);
                
                for(int[] array : extremities){
                    makeBoundaries(raws,i,array[0],array[1],boundariesToBeAdd);
                }
                
                TimeDebug.timeStop(6);
            }
            
            addCreatedBoundariesToPolygon(boundariesToBeAdd,bounds);
        }
        
        _boundPolygons = bounds;
    }
    
    public List<Boundary> getBoundaries(){
        return _boundaries;
    }
    
    public BoundPolygon[] getBoundPolygon(){
        return _boundPolygons;
    }

    private void addCreatedBoundariesToPolygon(Map<Integer, List<Boundary>> boundariesToBeAdd, BoundPolygon[] bounds) {
        int boundIndex;
        List<Boundary> listeBoundaries;

        for(Entry<Integer, List<Boundary>> entry : boundariesToBeAdd.entrySet()) {
            boundIndex = entry.getKey();
            listeBoundaries = entry.getValue();
            for(Boundary b : listeBoundaries){
                bounds[boundIndex].getBoundaries().add(b);
            }
        }
        
        boundariesToBeAdd.clear();
    }

    private void makeBoundaries(RawPolygon[] raws, int rawPolygonIndex, Map<Integer,List<Boundary>> boundariesToBeAdd) {
        makeBoundaries(raws, rawPolygonIndex, 0, raws[rawPolygonIndex].getPoints().size() - 1, boundariesToBeAdd);
    }
    
    private void makeBoundaries(RawPolygon[] raws, int rawPolygonIndex, int firstPointIndex, int lastPointIndex, Map<Integer,List<Boundary>> boundariesToBeAdd) {
        RawPolygon polygon = raws[rawPolygonIndex];
        int nbPoints = polygon.getPoints().size();
        
        Point previous_pt, current_pt, first_pt;

        first_pt = polygon.getPoints().get(firstPointIndex);

        List<Integer> polyWithPreviousPt;
        List<Integer> polyWithCurrentPt = getRegionsWhichContains(first_pt);
        
        int nbPolyWithPreviousPt;
        int nbPolyWithCurrentPt = polyWithCurrentPt.size();
        
        Boundary boundary_tmp = null;
        
        current_pt = first_pt;
        boolean _break = false;
        
        TimeDebug.timeStart(4);
        for(int i=(firstPointIndex+1)%nbPoints ; !_break; i = (i+1)%nbPoints){

            TimeDebug.timeStart(7);
            
            previous_pt = current_pt;
            current_pt = polygon.getPoints().get(i);
            
            if (i == lastPointIndex) {
                _break = true;
            }            
            
            if (current_pt.equals(previous_pt)) {
                continue;
            }
            
            TimeDebug.timeStop(7);
            
            TimeDebug.timeStart(8);

            polyWithPreviousPt = polyWithCurrentPt;
            polyWithCurrentPt = getRegionsWhichContains(current_pt);
            
            nbPolyWithPreviousPt = nbPolyWithCurrentPt;
            nbPolyWithCurrentPt = polyWithCurrentPt.size();

            TimeDebug.timeStop(8);
            
            TimeDebug.timeStart(3);
            
            int path;
            
            if (_break){
                path = 1;
                
                // à arranger
                if(nbPolyWithCurrentPt == nbPolyWithPreviousPt || !isSame(polyWithPreviousPt,polyWithCurrentPt)){
                    path = 4;
                }
                
                if(nbPolyWithCurrentPt != nbPolyWithPreviousPt || !isOneSubFromOther(polyWithCurrentPt,polyWithPreviousPt)){
                    path = 4;
                }
            }
            else if (nbPolyWithCurrentPt > nbPolyWithPreviousPt) {
                if(isOneSubFromOther(polyWithCurrentPt,polyWithPreviousPt)){
                    path = 1;
                }
                else{
                    path = 2;
                }
            } else if (nbPolyWithCurrentPt == nbPolyWithPreviousPt) {
                if(isSame(polyWithPreviousPt,polyWithCurrentPt)){
                    path = 3;
                }
                else{
                    path = 2;
                }
            } else {
                path = 2;
            }
            
            switch (path) {
                case 1:
                    boundary_tmp = startOrContinueBoundary(boundary_tmp, previous_pt);
                    boundary_tmp = finalizeBoundary(boundary_tmp, current_pt, boundariesToBeAdd, polyWithPreviousPt);
                    break;
                case 2:
                    boundary_tmp = finalizeBoundary(boundary_tmp, previous_pt, boundariesToBeAdd, polyWithPreviousPt);
                    boundary_tmp = startOrContinueBoundary(boundary_tmp, previous_pt);
                    break;
                case 3:
                    boundary_tmp = startOrContinueBoundary(boundary_tmp, previous_pt);
                    break;
                case 4:
                    boundary_tmp = finalizeBoundary(boundary_tmp, previous_pt, boundariesToBeAdd, polyWithPreviousPt);
                    break;
            }
            
            TimeDebug.timeStop(3);

        }
        TimeDebug.timeStop(4);
    }
    
    private boolean isSame(List<Integer> la, List<Integer> lb){
        if(la.size() != lb.size()){
            return false;
        }
        for(int i : la){
            if(!(lb.contains(i))){
                return false;
            }
        }
        return true;
    }
    
    private boolean isOneSubFromOther(List<Integer> listA, List<Integer> listB){
        List<Integer> petite;
        List<Integer> grande;
        if(listA.size() < listB.size()){
            petite = listA;
            grande = listB;
        }
        else{
            petite = listB;
            grande = listA;
        }
        
        for(int i : petite){
            if(!(grande.contains(i))){
                return false;
            }
        }
        return true;
    }
    
    private List<Integer> getRegionsWhichContains(Point p){
        TimeDebug.timeStart(15);
        
        List<Integer> region_indexes = _seeker.getContainingPolygon(p);
        
        TimeDebug.timeStop(15);
        

        return region_indexes;
    }

    private Boundary startOrContinueBoundary(Boundary boundary_tmp, Point point) {
        if(boundary_tmp == null){
            boundary_tmp = new Boundary();
        }
        boundary_tmp.getPoints().add(point);
        return boundary_tmp;
    }

    private Boundary finalizeBoundary(Boundary boundary_tmp, Point point, Map<Integer, List<Boundary>> boundariesToBeAdd, List<Integer> polygonsIndex){
        if(boundary_tmp != null){
            boundary_tmp.getPoints().add(point);
            for(int polygonIndex : polygonsIndex){
                if (boundariesToBeAdd.containsKey(polygonIndex)) {
                    boundariesToBeAdd.get(polygonIndex).add(boundary_tmp);
                } else {
                    List<Boundary> list = new ArrayList<>();
                    list.add(boundary_tmp);
                    boundariesToBeAdd.put(polygonIndex, list);
                }
            }
            _boundaries.add(boundary_tmp);
        }
        return null;
    }
    
    private List<int[]> getExtremitiesOfBoundariesToBeMade(BoundPolygon boundPoly, RawPolygon rawPoly){
        List<int[]> extremities = new ArrayList<>();
        
        List<Boundary> remainingBoundaries = boundPoly.getBoundaries().stream().collect(Collectors.toList());
        
        int nbPoints = rawPoly.getPoints().size() - 1;

        Boundary firstBoundary = remainingBoundaries.get(0);
        
        int indexOfFirstPointOfFirstBoundary = rawPoly.getPoints().indexOf(firstBoundary.getStartingPoint());

        if(indexOfFirstPointOfFirstBoundary == -1){
            Point p = firstBoundary.getStartingPoint();
            List<Integer> l = _seeker.getContainingPolygon(p);
            System.err.println("Point : "+p);
            System.err.print("Contenus dans :");
            for(int i : l){
                System.err.print(" ["+i+"]");
            }
            System.err.println();
            System.exit(1);
        }
        
        int currentPointIndex, lastPointIndex;

        if(rawPoly.getPoints().get((indexOfFirstPointOfFirstBoundary+1)%nbPoints).equals(firstBoundary.getPoints().get(1))){
            currentPointIndex = (indexOfFirstPointOfFirstBoundary + firstBoundary.getPoints().size() - 1) % nbPoints;
            lastPointIndex = indexOfFirstPointOfFirstBoundary;
        }
        else{
            currentPointIndex = indexOfFirstPointOfFirstBoundary;
            lastPointIndex = (indexOfFirstPointOfFirstBoundary - (firstBoundary.getPoints().size() - 1)) % nbPoints;
            if(lastPointIndex < 0){
                lastPointIndex = nbPoints + lastPointIndex;
            }
        }
        remainingBoundaries.remove(0);
        
        int firstExtremity = currentPointIndex;
        boolean newExtremity = false;

        while(currentPointIndex != lastPointIndex){
            for(int i=0 ; i<remainingBoundaries.size() ; i++){
                Boundary b = remainingBoundaries.get(i);
                
                Point currentPoint = rawPoly.getPoints().get(currentPointIndex);
                boolean start = (currentPoint.equals(b.getStartingPoint()));
                boolean end = (currentPoint.equals(b.getEndingPoint()));
                if (start || end) {
                    if (firstExtremity != currentPointIndex) {
                        extremities.add(new int[]{firstExtremity, currentPointIndex});
                    }
                    currentPointIndex = (currentPointIndex + b.getPoints().size() - 1) % nbPoints;
                    remainingBoundaries.remove(i);
                    firstExtremity = currentPointIndex;
                    newExtremity = true;
                    break;
                }
                else{
                    newExtremity = false;
                }
            }
            if(!newExtremity){
                currentPointIndex = (currentPointIndex+1) % nbPoints;
            }
            if(remainingBoundaries.isEmpty()){
                currentPointIndex = lastPointIndex;
            }
        }
        if (firstExtremity != lastPointIndex) {
            extremities.add(new int[]{firstExtremity, lastPointIndex});
        }

        
        return extremities;
    }
        
    private class PointSeeker{
        private static final int ARRAY_SIZE = 1000;
        private final Container[][] array = new Container[ARRAY_SIZE+1][ARRAY_SIZE+1];
        private final double offsetX;
        private final double offsetY;
        private final double multiplicateurX;
        private final double multiplicateurY;

        public PointSeeker(RawPolygon[] raws){
            Point firstPoint = raws[0].getPoints().get(0);
            Double minX = firstPoint.x;
            Double maxX = firstPoint.x;
            Double minY = firstPoint.y;
            Double maxY = firstPoint.y;
            
            for(RawPolygon raw : raws){
                for(Point p : raw.getPoints()){
                    if(p.x < minX){
                        minX = p.x;
                    }
                    else if(p.x > maxX){
                        maxX = p.x;
                    }
                    
                    if(p.y < minY){
                        minY = p.y;
                    }
                    else if(p.y > maxY){
                        maxY = p.y;
                    }
                }
            }
            
            offsetX = 0 - minX;
            offsetY = 0 - minY;
            multiplicateurX = (maxX - minX)/ARRAY_SIZE;
            multiplicateurY = (maxY - minY)/ARRAY_SIZE;

            for(int y=0 ; y<=ARRAY_SIZE ; y++){
                for(int x=0 ; x<=ARRAY_SIZE ; x++){
                    array[x][y] = new Container();
                }
            }
            
            for(int i=0; i<raws.length ; i++){
                for(Point p : raws[i].getPoints()){
                    array[indexX(p.x)][indexY(p.y)].add(p, i);
                }
            }
        }
        
        public List<Integer> getContainingPolygon(Point p){
            return array[indexX(p.x)][indexY(p.y)].get(p);
        }
        
        private int indexX(double x){
            return (int)((x + offsetX)/multiplicateurX);
        }
        
        private int indexY(double y){
            return (int)((y + offsetY)/multiplicateurY);
        }
        
        private class Container{
            private final Map<Point,List<Integer>> map;
            
            public Container(){
                map = new HashMap<>();
            }
            
            public void add(Point p, int polygonIndex){
                if(map.containsKey(p)){
                    List<Integer> list = map.get(p);
                    if(!list.contains(polygonIndex)){
                        list.add(polygonIndex);
                    }
                }
                else{
                    List<Integer> polygonsContainingPoint = new ArrayList<>();
                    polygonsContainingPoint.add(polygonIndex);
                    map.put(p, polygonsContainingPoint);
                }
            }
            
            public List<Integer> get(Point p){
                if(map.containsKey(p)){
                    return map.get(p);
                }
                else{
                    return null;
                }
            }
        }
    }
}