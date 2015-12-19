package Loader;

import Entities.Map;
import Entities.RawPolygon;
import Entities.Region;
import Entities.RegionManager;
import com.hexiong.jdbf.DBFReader;
import com.hexiong.jdbf.JDBFException;
import exception.InvalidMapException;
import org.nocrala.tools.gis.data.esri.shapefile.ValidationPreferences;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Chargeur des fichiers .shp et .dbf
 * @author Théophile
 */
public class MapLoader {
    private final DBFReader dbfReader;
    private final ShapeStreamReader shapeStreamReader;

    /**
     * Construit un chargeur de fichiers .shp et .dbf
     * @param shpFilePath Emplacement du fichier .shp
     * @param dbfFilePath Emplacement du fichier .dbf
     * @throws IOException
     * @throws InvalidMapException
     * @throws InvalidShapeFileException
     * @throws JDBFException
     */
    public MapLoader(String shpFilePath, String dbfFilePath) throws IOException, InvalidMapException, InvalidShapeFileException, JDBFException {
        FileReader fileReader = new FileReader(shpFilePath);
        
        ValidationPreferences v = new ValidationPreferences();
        v.setAllowUnlimitedNumberOfPointsPerShape(true);
        
        this.shapeStreamReader = new ShapeStreamReader(fileReader.getFileInputStream(),v);
        if(dbfFilePath != null){
            this.dbfReader = new DBFReader(dbfFilePath);
        }
        else {
            this.dbfReader = null;
        }
    }
    
    /**
     * Construit un chargeur avec uniquement un fichier .shp
     * @param shpFilePath Emplacement du fichier .shp
     * @throws IOException
     * @throws InvalidMapException
     * @throws InvalidShapeFileException
     * @throws JDBFException
     */
    public MapLoader(String shpFilePath) throws IOException, InvalidMapException, InvalidShapeFileException, JDBFException {
        this(shpFilePath, null);
    }
    
    /**
     * Charge une liste de régions à partir des données du/des fichier(s)
     * @return Structure carte contenant toutes les régions et les dimensions de la carte
     * @throws IOException
     * @throws InvalidShapeFileException
     * @throws JDBFException
     */
    public Map load() throws JDBFException, IOException, InvalidShapeFileException {
        RegionManager rm = loadRegions();
        
        return new Map(shapeStreamReader.getMapSizeX(), shapeStreamReader.getMapSizeY(), rm);
    }

    private RegionManager loadRegions() throws IOException, InvalidShapeFileException, JDBFException {
        List<List<RawPolygon>> rawRegions = new ArrayList<>();

        List<RawPolygon> rawRegion = shapeStreamReader.getNextShape();

        while(rawRegion.size() > 0){
            rawRegions.add(rawRegion);
            rawRegion = shapeStreamReader.getNextShape();
        }

        RegionManager rm = new RegionManager(rawRegions);

        for(Region r : rm.getRegions()){
            getdbfInfos(r);
        }
        
        return rm;
    }
    
    /*
    private BoundaryManager makeBoundaries(List<Region> regions){
        
        int nbRegions = regions.size();
        RawPolygon[] raws = new RawPolygon[regions.size()];
        for(int i=0 ; i<nbRegions ; i++){
            raws[i] = regions.get(i).getMainPolygon();
        }
        
        BoundaryManager boundaryManager = new BoundaryManager(raws);

        BoundPolygon[] bounds = boundaryManager.getBoundsPolygon();
        for(int i=0 ; i<nbRegions ; i++){
            regions.get(i).setBoundPolygon(bounds[i]);
        }
        return boundaryManager;
    }
    */

    private void getdbfInfos(Region region) throws JDBFException {
        if(dbfReader != null){
            if(dbfReader.hasNextRecord()){
                readdbfNextRecord(region);
            }
            else{
                System.err.println("[Warning] Données manquantes dans le .dbf");
            }
        }
    }

    private void readdbfNextRecord(Region region) throws JDBFException {
        Object[] data = dbfReader.nextRecord();
        for (int fieldIndex=0; fieldIndex < dbfReader.getFieldCount(); fieldIndex++)
        {
            String fieldName = dbfReader.getField(fieldIndex).getName();
            region.setInfo(fieldName, data[fieldIndex].toString());
        }
    }


}
