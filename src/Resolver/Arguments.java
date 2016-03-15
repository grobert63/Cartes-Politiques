package Resolver;

import Entities.HexGrid;
import Entities.Region;

/**
 * File : Resolver.Arguments.java
 * Created by Julien Defiolles on 07/03/2016.
 * All Rights Reserved Guillaume Robert & Maxime Lemort & Julien Defiolles & Theophile Pumain
 */
public class Arguments {
    private HexGrid hexGrid = null;
    private Region region = null;
    private int direction = 0;

    public Arguments() {
    }

    public HexGrid getHexGrid() {
        return hexGrid;
    }

    public void setHexGrid(HexGrid hexGrid) {
        this.hexGrid = hexGrid;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
