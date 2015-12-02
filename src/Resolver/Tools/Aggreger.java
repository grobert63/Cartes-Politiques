/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    Map<Index2,Region> map = new HashMap<>();
    
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
        return !map.containsKey(getDirectionIndex(idx,direction));
    }
    
    public List<Region> getAggregatedRegion(){
        return new ArrayList(map.values());
    }
    
    public void add(Region r, Region reference, int direction){//Index2 source, Region nearest){
        Index2 ref = getIndex(reference);
        Index2 idx = getDirectionIndex(ref, direction);
        addInMap(idx, r);
    }
    
    private void addInMap(Index2 idx, Region r){
        if(idx.x < minX) minX = idx.x;
        if(idx.x > maxX) maxX = idx.x;
        if(idx.y < minY) minY = idx.y;
        if(idx.y > maxY) maxY = idx.y;
        //r.setData(r.getDefaultField(), idx.toString());
        map.put(idx, r);
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

