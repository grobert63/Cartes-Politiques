package Resolver;

import Entities.Direction;
import Entities.HexGrid;
import Entities.Region;
import Resolver.Tools.Aggreger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Algorithme à revoir)
 * Cet algorithme de résolution fonctionne de la manière suivante :
 * • il prend les deux régions les plus proches et les assemble
 * • tant qu'il reste des régions non assemblées :
 *     - il prend la région non assemblée la plus proche de l'une des régions déjà assemblée, et l'assemble.
 * @author Théophile
 */
public class SimpleAggregerResolver implements IResolver{
    Aggreger aggreger = null;
    List<Region> isolated = new ArrayList<>();
    
    int minX=0, maxX=0, minY=0, maxY=0;

    private boolean isAllAggregated(){
        return isolated.isEmpty();
    }
    
    private void aggregate(){        
        double distance, min_dist = -1.0;
        Region nearest = null;
        if(aggreger == null){
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
            aggreger = new Aggreger(source);
            aggreger.add(nearest, source, getDirection(source, nearest));
            isolated.remove(source);
            isolated.remove(nearest);

        }
        else{
            Region source = null;
            for (Region r : aggreger.getAggregatedRegion()){
                if(aggreger.hasPlaceAround(r)){
                    for (Region anIsolated : isolated) {
                        distance = r.getDistanceTo(anIsolated);
                        if (min_dist == -1.0 || distance < min_dist) {
                            source = r;
                            nearest = anIsolated;
                            min_dist = distance;
                        }
                    }
                }
            }
            //System.out.println(map.get(source).getName() + " " + source+ " -> "+nearest.getName());
            aggreger.add(nearest, source, getDirection(source, nearest));
            isolated.remove(nearest);
        }
    }
    
    private int getDirection(Region source, Region nearest){
        double angle = source.getAngleTo(nearest);
        int direction = Direction.getDirectionFromAngle(angle);
        while(!aggreger.hasPlace(source,direction)){
            direction = (direction + 1) % 6;
        }
        return direction;
    }
    
    @Override
    public HexGrid resolve(List<Region> list){
        isolated.addAll(list.stream().collect(Collectors.toList()));
        
        while(!isAllAggregated()){
            aggregate();
        }
        return aggreger.toGrid();
    }
}
