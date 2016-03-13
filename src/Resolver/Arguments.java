package Resolver;

import Entities.Direction;
import Entities.HexGrid;
import Entities.Region;

/**
 * Created by PAYS on 07/03/2016.
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

    public Region getRegion() {
        return region;
    }



    public void setHexGrid(HexGrid hexGrid) {
        this.hexGrid = hexGrid;
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
