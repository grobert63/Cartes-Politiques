package Resolver.Tools;

import Entities.Direction;
import Entities.HexGrid;
import Entities.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Théophile
 */
public class Aggreger {
    final Map<Index2, Region> map = new HashMap<>();
    
    int minX=0, maxX=0, minY=0, maxY=0;
    
    public Aggreger(Region source){
        addInMap(new Index2(0,0), source);
    }
    
    private Index2 getIndex(Region r){
        for (Map.Entry<Index2, Region> entry : map.entrySet()){
            if(r == entry.getValue()){ return entry.getKey(); }
        }

        return null;
    }
    
    private Index2 getDirectionIndex(Index2 idx, int direction, int nb ){
        if(nb <= 1) nb =1;
        if(nb >=2) nb =2;
        switch(direction){
            case Direction.NORTH_EAST : 
                if(idx.y % 2 == 0)
                    return new Index2(idx.x + nb, idx.y - nb);
                else
                    return new Index2(idx.x, idx.y - nb);
            case Direction.EAST : //
                return new Index2(idx.x + nb, idx.y);
            case Direction.SOUTH_EAST : 
                if(idx.y % 2 == 0)
                    return new Index2(idx.x + nb, idx.y +nb);
                else
                    return new Index2(idx.x, idx.y + nb);
            case Direction.SOUTH_WEST : 
                if(idx.y % 2 == 0)
                    return new Index2(idx.x, idx.y + nb);
                else
                    return new Index2(idx.x - nb, idx.y + nb);
            case Direction.WEST : //
                return new Index2(idx.x - nb, idx.y);
            case Direction.NORTH_WEST : 
                if(idx.y % 2 == 0)
                    return new Index2(idx.x, idx.y - nb);
                else
                    return new Index2(idx.x - nb, idx.y - nb);
        }
        return null;
    }
    
    public boolean hasPlaceAround(Region r){
        for(int direction=0 ; direction < 6 ; direction++){
            if(hasPlace(r,direction)){
                return true;
            }
        }
        return false;
    }
    
    public boolean hasPlace(Region r, int direction){    
        Index2 idx = getIndex(r);
        if(idx == null) return false;
        return !map.containsKey(getDirectionIndex(idx,direction,1));
    }
    
    public List<Region> getAggregatedRegion(){
        return new ArrayList<>(map.values());
    }
    
    public void add(Region r, Region reference, int direction){//Index2 source, Region nearest){
        if(getIndex(r) != null) return;
        Index2 ref = getIndex(reference);
         Index2 idx = getDirectionIndex(ref, direction,1);
            addInMap(idx, r);
    }
    
    private void addInMap(Index2 idx, Region r){
        if(idx.x < minX) minX = idx.x;
        if(idx.x > maxX) maxX = idx.x;
        if(idx.y < minY) minY = idx.y;
        if(idx.y > maxY) maxY = idx.y;
        map.put(idx, r);
    }

    public void decaler(Region r, Region reference, int direction,double distance)
    {
        if(getIndex(r) != null) return;
        Index2 idx = getIndex(reference);
        if(r.IsCommunBoundary(reference)) distance = 1;
        Index2 idxf = getDirectionIndex(idx,direction,(int)(distance));
        idx =idxf;
        int nb =0;
        int min = 999 ;
        int dir = 0;
        Index2 idxDernier = idx;

        while(map.containsKey(getDirectionIndex(idx,direction,1)))
        {
            nb++;
            idx = getDirectionIndex(idx,direction,1);
        }

        if(nb < min)
        {
            idxDernier = idx;
            min = nb;
            dir = direction;
        }

        for(int i =0; i<= min ;i++)
        {
            try {
                addInMap(getDirectionIndex(idxDernier,dir,1),map.get(idxDernier));
                idxDernier = getDirectionIndex(idxDernier,Direction.getOpposite(dir),1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        addInMap(idxf,r);
    }

    public boolean exist(Region r)
    {
        if(getIndex(r) == null) return false;
        return true;
    }

    public HexGrid toGrid(){
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
            return !(this.x != other.x || this.y != other.y);
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

