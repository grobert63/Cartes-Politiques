package SHPDecoder;

import exception.InvalidMapException;
import javafx.scene.shape.Polygon;
import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.ValidationPreferences;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.ShapeType;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;

import java.io.FileInputStream;
import java.io.IOException;


/**
 * Classe permettant la lecture d'un fichier shapefile pour en recuperer les informations globales et les polygones representant chaque pays
 */
public class ShapeStreamReader {
    private ShapeFileReader reader;

    /**
     * Constructeur de la classe
     * @param stream Flux du fichier shapefile a lire
     * @throws InvalidShapeFileException Le flux n'est pas un fichier shapefile valide
     * @throws IOException Erreur de lecture du flux de donnees
     * @throws InvalidMapException Le format de la carte n'est pas reconnu
     * @throws NullPointerException Le flux de donnees est invalide ou absent
     */
    public ShapeStreamReader(FileInputStream stream) throws InvalidShapeFileException, IOException, InvalidMapException, NullPointerException {
        this(stream,null);
    }

    public ShapeStreamReader(FileInputStream stream, ValidationPreferences validationPreferences) throws InvalidShapeFileException, IOException, InvalidMapException, NullPointerException {
        if (stream != null) {
            if (validationPreferences == null)
            {
                createShapeFileReader(stream);
            }
            else
            {
                createShapeFileReader(stream,validationPreferences);
            }
        } else {
            reader = null;
            throw new NullPointerException("Le flux de données est absent");
        }
    }

    private void createShapeFileReader(FileInputStream stream) throws InvalidShapeFileException, IOException, InvalidMapException {
        try {
            reader = new ShapeFileReader(stream);
        } catch (InvalidShapeFileException e) {
            throw new InvalidShapeFileException("La forme est invalide", e.getCause());
        } catch (IOException e) {
            throw new IOException("Une erreur de lecture est survenue", e.getCause());
        }
        isMapValid(reader);
    }

    private void createShapeFileReader(FileInputStream stream,ValidationPreferences validationPreferences) throws InvalidShapeFileException, IOException, InvalidMapException {
        try {
            reader = new ShapeFileReader(stream,validationPreferences);
        } catch (InvalidShapeFileException e) {
            throw new InvalidShapeFileException("La forme est invalide", e.getCause());
        } catch (IOException e) {
            throw new IOException("Une erreur de lecture est survenue", e.getCause());
        }
        isMapValid(reader);
    }

    private boolean isMapValid(ShapeFileReader reader) throws InvalidMapException {
        if (reader.getHeader().getBoxMaxZ() != 0 || reader.getHeader().getBoxMinZ() != 0 || reader.getHeader().getBoxMaxM() != 0 || reader.getHeader().getBoxMinM() != 0) {
            throw new InvalidMapException("Seules les cartes 2D sont gérées");
        }
        if (reader.getHeader().getShapeType() != ShapeType.POLYGON){
            throw new InvalidMapException("Seuls les polygones sont gérés pour les régions");
        }
        return true;
    }


    /**
     * retourne la taille en X de la carte
     * @return taille en X
     */
    public double getMapSizeX() {
        return (Math.abs(reader.getHeader().getBoxMaxX()) - reader.getHeader().getBoxMinX());
    }

    /**
     * retourne la taille en Y de la carte
     * @return taille en Y
     */
    public double getMapSizeY() {
        return (Math.abs(reader.getHeader().getBoxMaxY()) - reader.getHeader().getBoxMinY());
    }

    /**
     * retourne la valeur minimale de X
     * @return minimum de X
     */
    public double getMapMinX() {
        return reader.getHeader().getBoxMinX();
    }

    /**
     * retourne la valeur minimale de Y
     * @return minimum de Y
     */
    public double getMapMinY() {
        return reader.getHeader().getBoxMinY();
    }

    /**
     * retourne la valeur maximale de X
     * @return maximum de X
     */
    public double getMapMaxX() {
        return reader.getHeader().getBoxMaxX();
    }

    /**
     * retourne la valeur maximale de Y
     * @return maximum de Y
     */
    public double getMapMaxY() {
        return reader.getHeader().getBoxMaxY();
    }

    /**
     * retourne le type de formes contenues dans le fichier shapefile
     * @return type des formes
     */
    public String getShapeName() {
        return reader.getHeader().getShapeType().name();
    }

    /**
     * affiche les infos globales sur le fichier dans la console
     */
    public void printInfos() {
        System.out.println("min (X) : " + getMapMinX());
        System.out.println("max (X) : " + getMapMaxX());
        System.out.println("size (X) : " + getMapSizeX());
        System.out.println("min (Y) : " + getMapMinY());
        System.out.println("max (Y) : " + getMapMaxY());
        System.out.println("size (Y) : " + getMapSizeY());
        System.out.println("Shape File : " + getShapeName());
    }

    public Polygon getNextShape() throws IOException, InvalidShapeFileException {
        AbstractShape shape = reader.next();
        Polygon polygon = new Polygon();
        if (shape != null)
        {
            PolygonShape polygonShape = (PolygonShape) shape;
            for (int i = 0; i < polygonShape.getNumberOfParts(); i++) {
                addPointDataArrayToPolygon(polygon,polygonShape.getPointsOfPart(i));
            }
        }

        return polygon;
    }

    private Polygon addPointDataArrayToPolygon(Polygon polygon, PointData[] points) {
        for (PointData point : points) {
            polygon.getPoints().add(point.getX() - getMapMinX());
            polygon.getPoints().add(getMapSizeY() - (point.getY() - getMapMinY()));
        }
        return polygon;
    }
}
