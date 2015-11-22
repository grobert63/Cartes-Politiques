package Resolver;

import Entities.Direction;
import Entities.HexGrid;
import Entities.Region;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (Algorithme à revoir)
 * Cet algorithme de résolution fonctionne de la manière suivante :
 * • il prend les deux régions les plus proches et les assemble
 * • tant qu'il reste des régions non assemblées :
 *     - il prend la région non assemblée la plus proche de l'une des régions déjà assemblée, et l'assemble.
 * @author Théophile
 */
public class SimpleAggregerResolver implements IResolver{
    Map<Index2,Region> map = new HashMap<>();
    List<Region> isolated = new ArrayList<>();
    
    int minX=0, maxX=0, minY=0, maxY=0;

    private boolean isAllAggregated(){
        return isolated.isEmpty();
    }
    
    private Index2 getDirectionIndex(Index2 idx, int direction){
        switch(direction){
            case Direction.NORTH_EAST : 
                if(idx.y % 2 == 0)
                    return new Index2(idx.x + 1, idx.y - 1);
                else
                    return new Index2(idx.x, idx.y - 1);
            case Direction.EAST : //
                return new Index2(idx.x + 1, idx.y);
            case Direction.SOUTH_EAST : 
                if(idx.y % 2 == 0)
                    return new Index2(idx.x + 1, idx.y + 1);
                else
                    return new Index2(idx.x, idx.y + 1);
            case Direction.SOUTH_WEST : 
                if(idx.y % 2 == 0)
                    return new Index2(idx.x, idx.y + 1);
                else
                    return new Index2(idx.x - 1, idx.y + 1);
            case Direction.WEST : //
                return new Index2(idx.x - 1, idx.y);
            case Direction.NORTH_WEST : 
                if(idx.y % 2 == 0)
                    return new Index2(idx.x, idx.y - 1);
                else
                    return new Index2(idx.x - 1, idx.y - 1);
        }
        return null;
    }
    
    private boolean hasPlaceAround(Index2 idx){
        for(int direction=0 ; direction < 6 ; direction++){
            if(hasPlace(idx,direction)){
                return true;
            }
        }
        return false;
    }
    
    private boolean hasPlace(Index2 idx, int direction){    
        return !map.containsKey(getDirectionIndex(idx,direction));
    }
    
    private void aggregate(){        
        double distance, min_dist = -1.0;
        Region nearest = null;
        if(map.isEmpty()){
            Region source = null;
            for(int i=0 ; i < isolated.size() ; i++){
                for(int j = i+1; j<isolated.size();j++){
                    distance = isolated.get(i).getDistanceTo(isolated.get(j));
                    if(min_dist == -1.0 || distance < min_dist){
                        source = isolated.get(i);
                        nearest = isolated.get(j);
                        min_dist = distance;
                    }
                }
            }
            firstAggregation(source, nearest);
        }
        else{
            Index2 source = null;
            for (Map.Entry<Index2, Region> entry : map.entrySet()){
                Index2 idx = entry.getKey();
                Region r = entry.getValue();
                if(hasPlaceAround(idx)){
                    for(int i=0 ; i < isolated.size() ; i++){
                        distance = r.getDistanceTo(isolated.get(i));
                        if(min_dist == -1.0 || distance < min_dist){
                            source = idx;
                            nearest = isolated.get(i);
                            min_dist = distance;
                        }
                    }
                }
            }
            //System.out.println(map.get(source).getName() + " " + source+ " -> "+nearest.getName());
            optimizeAggregation(source, nearest);
        }
    }
    
    private void firstAggregation(Region source, Region nearest){
        double angle = source.getAngleTo(nearest);
        int direction = Direction.getDirectionFromAngle(angle);
        Index2 origin = new Index2(0,0);
        
        addInMap(origin, source);
        addInMap(getDirectionIndex(origin,direction), nearest);
    }
    
    private void optimizeAggregation(Index2 source, Region nearest){
        double angle = map.get(source).getAngleTo(nearest);
        int direction = Direction.getDirectionFromAngle(angle);
        while(!hasPlace(source,direction)){
            direction = (direction + 1) % 6;
        }
        
        addInMap(getDirectionIndex(source,direction), nearest);
    }
    
    private void addInMap(Index2 idx, Region r){
        if(idx.x < minX) minX = idx.x;
        if(idx.x > maxX) maxX = idx.x;
        if(idx.y < minY) minY = idx.y;
        if(idx.y > maxY) maxY = idx.y;
        //r.setData(r.getDefaultField(), idx.toString());
        map.put(idx, r);
        isolated.remove(r);
    }

    private HexGrid toGrid(Map<Index2,Region> map){
        int width = maxX-minX+1;
        int height = maxY-minY+1;
        int decalageX = -minX, decalageY = -minY;
        
        HexGrid grid = new HexGrid(width, height);
        
        for (Map.Entry<Index2, Region> entry : map.entrySet()){
            Index2 idx = entry.getKey();
            Region r = entry.getValue();
            grid.addRegion(idx.x+decalageX, idx.y+decalageY, r);
        }
        
        return grid;
    }
    
    @Override
    public HexGrid resolve(List<Region> list){
        for(Region r : list){
            isolated.add(r);
        }
        
        while(!isAllAggregated()){
            aggregate();
        }
        return toGrid(this.map);
    }
    
    private class Index2{
        public final int x;
        public final int y;

        public Index2(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Index2 other = (Index2) obj;
            if (this.x != other.x || this.y != other.y) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return x*43797 + y;
        }

        @Override
        public String toString() {
            return "{"+x+","+y+"}";
        } 
    }
    
}
