package Loader;

import Entities.Region;
import SHPDecoder.FileReader;
import SHPDecoder.ShapeStreamReader;
import com.hexiong.jdbf.DBFReader;
import com.hexiong.jdbf.JDBFException;
import exception.InvalidMapException;
import javafx.scene.shape.Polygon;
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
     * @return Liste de régions
     * @throws IOException
     * @throws InvalidMapException
     * @throws InvalidShapeFileException
     * @throws JDBFException
     */
    public List<Region> load() throws IOException, InvalidMapException, InvalidShapeFileException, JDBFException {
        List<Region> list = new ArrayList<>();
        
        Polygon polygon = shapeStreamReader.getNextShape();
        while (polygon.getPoints().size() != 0 ){
            Region region = new Region(polygon);
            getdbfInfos(region);
            list.add(region);
            polygon = shapeStreamReader.getNextShape();
        }
        return list;
    }

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
            region.setData(fieldName, data[fieldIndex].toString());
        }
    }


}
