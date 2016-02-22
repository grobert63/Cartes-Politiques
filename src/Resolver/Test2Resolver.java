package Resolver;

import Entities.Boundary;
import Entities.Direction;
import Entities.HexGrid;
import Entities.Region;
import Resolver.Tools.Aggreger;
import com.sun.javafx.geom.Vec2d;
import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * Created by PAYS on 08/02/2016.
 */
    public class Test2Resolver {
    final List<Region> isolated = new ArrayList<>();
    Aggreger aggreger = null;
    int minX=0, maxX=0, minY=0, maxY=0;

    private void firstAggregate() {
        double distance, min_dist = -1.0;
        Region nearest = null;
        Region source = null;
        for (int i = 0; i < isolated.size(); i++) {
            for (int j = i + 1; j < isolated.size(); j++) {
                distance = isolated.get(i).getDistanceTo(isolated.get(j));
                if (min_dist == -1.0 || distance < min_dist) {
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

    private Region aggregate(Region region,int firstDirection,boolean clok) {
        double distance, min_dist = -1.0;
        Region nearest = null;
        Region retour[] = new Region[6];
        isolated.remove(region);
        List<Region> coucou = new ArrayList<>();
        for (int direction:Direction.getAllDirection()) {
            if(aggreger.hasPlace(region,direction)) {
                for (Region r : isolated) {
                    int dire = getDirection(region, r);

                    if (dire == direction) {
                        coucou.add(r);
                    }
                }
                for (Region r : coucou) {
                    distance = region.getDistanceTo(r);
                    if (min_dist == -1 || min_dist > distance) {
                        min_dist = distance;
                        nearest = r;
                    }
                }

                if (nearest != null && region.IsCommunBoundary(nearest)) {
                    retour[direction] = nearest;
                    aggreger.add(nearest, region, direction);
                }
            }
        }
        if(clok)
            for (int i = 0 ; i <6 ;i++)
            {
                Region r = retour[(i+firstDirection)%6];
                if( r !=null) return  r;
            }
        else
            for (int i = 0 ; i <6 ;i++)
            {
                Region r = null;
                if(firstDirection-i>= 0 )
                    r = retour[firstDirection-i];
                else
                    r = retour[6+(firstDirection-i)];
                if( r !=null) return  r;
            }
        return null;
    }

    private int getDirection(Region source, Region nearest){
        double angle = source.getAngleTo(nearest);
        int direction = Direction.getDirectionFromAngle(angle);
        return direction;
    }

    public Region addRegion(Region region)
    {
        if(aggreger.exist(region)) return region;
        List<Region> coucou = new ArrayList<>();
        double distance;
        double min_dist = -1;
        Region nearest = null;
        for (int direction:Direction.getAllDirection()) {
            for (Region r : aggreger.getAggregatedRegion()) {
                int dire = getDirection(region, r);

                if (dire == direction) {
                    coucou.add(r);
                }
            }
            for (Region r : coucou) {
                distance = region.getDistanceTo(r);
                if (min_dist == -1 || min_dist > distance) {
                    min_dist = distance;
                    nearest = r;
                }
            }
            if(nearest != null && region.IsCommunBoundary(nearest))
            {
                aggreger.add(region, nearest, direction);
                return region;
            }
        }
        return region;
    }

    private void createRegion(Region region) throws Exception {
        double distance,min_dist =-1.0;
        Region nearest = null;
        if(aggreger.exist(region)) return;
        for (Region r: aggreger.getAggregatedRegion()) {
            distance =999;
            if(r != null)
                distance = region.getDistanceTo(r);
            if (min_dist == -1 || min_dist > distance) {
                min_dist = distance;
                nearest = r;
            }
        }
        aggreger.decaler(region,nearest,Direction.getOpposite(getDirection(region,nearest)),min_dist);
    }

    public HexGrid resolve(List<Region> list,int DirectionFirst, boolean ClockWork,Region first,int nbTour) {
        isolated.addAll(list.stream().collect(Collectors.toList()));
        Region regionSuivante = first;
        aggreger = new Aggreger(regionSuivante);
        int test=0;
        while(!isolated.isEmpty() && test != nbTour) {
            while (regionSuivante != null && (test<nbTour || nbTour == -1)) {
                regionSuivante = aggregate(regionSuivante,DirectionFirst,ClockWork);
                test++;
            }
            if(!isolated.isEmpty() && test != nbTour) {
                try {
                    createRegion(isolated.get(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                regionSuivante = isolated.get(0);
            }
        }
        return aggreger.toGrid();
    }
}
