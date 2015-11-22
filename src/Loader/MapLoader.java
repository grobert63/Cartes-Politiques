package Loader;

import Entities.Region;
import com.hexiong.jdbf.DBFReader;
import com.hexiong.jdbf.JDBFException;
import exception.InvalidMapException;
import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.ValidationPreferences;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.nocrala.tools.gis.data.esri.shapefile.shape.ShapeType.POLYGON;

/**
 * Chargeur des fichiers .shp et .dbf
 * @author Théophile
 */
public class MapLoader {
    private final ShapeFileReader shpReader;
    private final DBFReader dbfReader;

    /**
     * Construit un chargeur de fichiers .shp et .dbf
     * @param shpFilePath Emplacement du fichier .shp
     * @param dbfFilePath Emplacement du fichier .dbf
     * @throws Exception 
     */
    public MapLoader(String shpFilePath, String dbfFilePath) throws Exception{
        FileInputStream is = new FileInputStream(shpFilePath);
        
        ValidationPreferences v = new ValidationPreferences();
        v.setAllowUnlimitedNumberOfPointsPerShape(true);
        this.shpReader = new ShapeFileReader(is,v);
        
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
     * @throws Exception
     */
    public MapLoader(String shpFilePath) throws Exception{
        this(shpFilePath, null);
    }
    
    /**
     * Charge une liste de régions à partir des données du/des fichier(s)
     * @return Liste de régions
     * @throws InvalidMapException
     */
    public List<Region> load() throws InvalidMapException, IOException, InvalidShapeFileException, JDBFException {
        List<Region> list = new ArrayList<>();
        
        AbstractShape s;
        while ((s = shpReader.next()) != null){
            if(s.getShapeType() != POLYGON){
                throw new InvalidMapException("La classe MapLoader ne gère pas les formes de type "+s.getShapeType());
            }
            
            PolygonInfo pi = new PolygonInfo((PolygonShape) s);
            PointData centre = pi.getCentreDeMasse();
            
            Region region = new Region(centre.getX(),centre.getY());
            
            if(dbfReader != null){
                if(dbfReader.hasNextRecord()){
                    Object[] data = dbfReader.nextRecord();
                    for (int fieldIndex=0; fieldIndex < dbfReader.getFieldCount(); fieldIndex++)
                    {
                        String fieldName = dbfReader.getField(fieldIndex).getName();
                        region.setData(fieldName, data[fieldIndex].toString());
                    }
                }
                else{
                    System.err.println("[Warning] Données manquantes dans le .dbf");
                }
            }
            
            list.add(region);
        }
        return list;
    }
}
