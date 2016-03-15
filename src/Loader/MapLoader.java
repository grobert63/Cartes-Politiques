package Loader;

import CustomException.InvalidMapException;
import Entities.GeoMap;
import Entities.RawPolygon;
import Entities.Region;
import Entities.RegionManager;
import LoggerUtils.LoggerManager;
import com.hexiong.jdbf.DBFReader;
import com.hexiong.jdbf.JDBFException;
import org.nocrala.tools.gis.data.esri.shapefile.ValidationPreferences;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Chargeur des fichiers .shp et .dbf
 *
 * @author Théophile
 */
public class MapLoader {
    private final DBFReader dbfReader;
    private final ShapeStreamReader shapeStreamReader;

    /**
     * Construit un chargeur de fichiers .shp et .dbf
     *
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

        this.shapeStreamReader = new ShapeStreamReader(fileReader.getFileInputStream(), v);
        if (dbfFilePath != null) {
            this.dbfReader = new DBFReader(dbfFilePath);
        } else {
            this.dbfReader = null;
        }
    }

    /**
     * Charge une liste de régions à partir des données du/des fichier(s)
     *
     * @return Structure carte contenant toutes les régions et les dimensions de la carte
     * @throws IOException
     * @throws InvalidShapeFileException
     * @throws JDBFException
     */
    public GeoMap load() throws JDBFException, IOException, InvalidShapeFileException {
        RegionManager manager = loadRegions();

        return new GeoMap(shapeStreamReader.getMapSizeX(), shapeStreamReader.getMapSizeY(), manager);
    }

    public DBFReader getDbfReader() {
        return dbfReader;
    }

    private RegionManager loadRegions() throws IOException, InvalidShapeFileException, JDBFException {
        List<List<RawPolygon>> rawRegions = new ArrayList<>();

        List<RawPolygon> rawRegion = shapeStreamReader.getNextShape();

        while (rawRegion.size() > 0) {
            rawRegions.add(rawRegion);
            rawRegion = shapeStreamReader.getNextShape();
        }

        RegionManager manager = new RegionManager(rawRegions);

        for (Region r : manager.getRegions()) {
            getdbfInfos(r);
        }

        return manager;
    }

    private void getdbfInfos(Region region) throws JDBFException {
        if (dbfReader != null) {
            if (dbfReader.hasNextRecord()) {
                readdbfNextRecord(region);
            } else {
                LoggerManager.getInstance().getLogger().log(Level.WARNING, "Missing data in the DataBase File (.dbf)");
            }
        }
    }

    private void readdbfNextRecord(Region region) throws JDBFException {
        Object[] data = dbfReader.nextRecord();
        for (int fieldIndex = 0; fieldIndex < dbfReader.getFieldCount(); fieldIndex++) {
            String fieldName = dbfReader.getField(fieldIndex).getName();
            region.setInfo(fieldName, data[fieldIndex].toString());
        }
    }
}
