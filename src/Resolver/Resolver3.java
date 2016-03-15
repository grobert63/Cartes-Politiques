package Resolver;

import Entities.Direction;
import Entities.HexGrid;
import Entities.Region;

import java.util.List;

/**
 * File : Resolver.Resolver3.java
 * Created by Julien Defiolles on 07/03/2016.
 * All Rights Reserved Guillaume Robert & Maxime Lemort & Julien Defiolles & Theophile Pumain
 */
public class Resolver3 {

    private final Test2Resolver test2Resolve = new Test2Resolver();

    public Arguments resolve(List<Region> list) {
        HexGrid grid;
        double max = 0;
        double nb2 = 0;
        Arguments arguments = new Arguments();
        for (Region r: list) {
            for (int direction : Direction.getAllDirection())
            {
                grid = test2Resolve.resolve(list,direction,true,r,-1);
                for (int i = 0 ; i < grid.getWidth() ; i++)
                {
                    for(int j = 0 ; j < grid.getHeight() ; j++)
                    {
                        Region region = grid.getRegion(i,j);
                        Region region2;
                        if(region == null) break;
                        if(i - 1 >= 0 )
                        {
                            if((region2 = grid.getRegion(i-1,j)) != null) {
                                nb2 = updateNb2(nb2, region2.IsCommunBoundary(region));
                            }

                            if( j - 1 >= 0)
                            {
                                if((region2 = grid.getRegion(i-1,j-1)) != null) {
                                    nb2 = updateNb2(nb2, region2.IsCommunBoundary(region));
                                }
                            }

                            if( j + 1 < grid.getHeight())
                            {
                                if((region2 = grid.getRegion(i-1,j+1)) != null) {
                                    nb2 = updateNb2(nb2, region2.IsCommunBoundary(region));
                                }
                            }
                        }

                        if(i + 1 < grid.getWidth())
                        {
                            if((region2 = grid.getRegion(i+1,j)) != null) {
                                nb2 = updateNb2(nb2, region2.IsCommunBoundary(region));
                            }
                        }

                        if( j - 1 >= 0)
                        {
                            if((region2 = grid.getRegion(i,j-1)) != null) {
                                nb2 = updateNb2(nb2, region2.IsCommunBoundary(region));
                            }
                        }

                        if( j + 1 < grid.getHeight())
                        {
                            if((region2 = grid.getRegion(i,j+1)) != null) {
                                nb2 = updateNb2(nb2, region2.IsCommunBoundary(region));
                            }
                        }
                    }
                }
                if(nb2 > max)
                {
                    max = nb2;
                    arguments.setHexGrid(grid);
                    arguments.setDirection(direction);
                    arguments.setRegion(r);
                }
                nb2 = 0;
            }
        }
        return arguments;
    }

    private double updateNb2(double nb2, boolean isCommonBoundary) {
        if (isCommonBoundary) {
            return nb2 + 1;
        } else {
            return nb2 - 0.2;
        }
    }
}
