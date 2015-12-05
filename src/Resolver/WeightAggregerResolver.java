package Resolver;

import Entities.HexGrid;
import Entities.Region;
import Resolver.Tools.Aggreger;
import Resolver.Tools.WeightArray;
import Resolver.Tools.WeightCalculator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Théophile
 */
public class WeightAggregerResolver implements IResolver{
    private Aggreger aggreger;
    private List<Region> isolated;
    private List<Region> regions;
    private WeightArray weightArray;
    
    @Override
    public HexGrid resolve(List<Region> list) {
        regions = list;
        isolated.addAll(list.stream().collect(Collectors.toList()));
        
        WeightCalculator wghtCalc = new WeightCalculator(regions);
        //weightArray = wghtCalc.getWeights();
        
        aggreger = new Aggreger(isolated.get(0));
        isolated.remove(0);
        
        while(!isolated.isEmpty()){
            //aggregate();
        }
        return aggreger.toGrid();
    }
    
}
