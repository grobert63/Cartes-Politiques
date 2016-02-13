package Loader;

import Entities.Point;
import Entities.RawPolygon;
import CustomException.InvalidMapException;
import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.ValidationPreferences;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.ShapeType;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.AbstractPolyShape;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe permettant la lecture d'un fichier shapefile pour en recuperer les informations globales et les polygones representant chaque pays
 */
public class ShapeStreamReader {
    private ShapeFileReader _reader;

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

    /**
     * Constructeur de la classe
     * @param stream Flux du fichier shapefile a lire
     * @param validationPreferences Preferences de lecture du fichier shapefile
     * @throws InvalidShapeFileException Le flux n'est pas un fichier shapefile valide
     * @throws IOException Erreur de lecture du flux de donnees
     * @throws InvalidMapException Le format de la carte n'est pas reconnu
     * @throws NullPointerException Le flux de donnees est invalide ou absent
     */
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
            _reader = null;
            throw new NullPointerException("Le flux de données est absent");
        }
    }

    private void createShapeFileReader(FileInputStream stream) throws InvalidShapeFileException, IOException, InvalidMapException {
        try {
            _reader = new ShapeFileReader(stream);
        } catch (InvalidShapeFileException e) {
            throw new InvalidShapeFileException("La forme est invalide", e.getCause());
        } catch (IOException e) {
            throw new IOException("Une erreur de lecture est survenue", e.getCause());
        }
        isMapValid(_reader);
    }

    private void createShapeFileReader(FileInputStream stream,ValidationPreferences validationPreferences) throws InvalidShapeFileException, IOException, InvalidMapException {
        try {
            _reader = new ShapeFileReader(stream,validationPreferences);
        } catch (InvalidShapeFileException e) {
            throw new InvalidShapeFileException("La forme est invalide", e.getCause());
        } catch (IOException e) {
            throw new IOException("Une erreur de lecture est survenue", e.getCause());
        }
        isMapValid(_reader);
    }

    private boolean isMapValid(ShapeFileReader reader) throws InvalidMapException {
        if (reader.getHeader().getBoxMaxZ() != 0 || reader.getHeader().getBoxMinZ() != 0 || reader.getHeader().getBoxMaxM() != 0 || reader.getHeader().getBoxMinM() != 0) {
            throw new InvalidMapException("Seules les cartes 2D sont gérées");
        }
        if (reader.getHeader().getShapeType() != ShapeType.POLYGON && reader.getHeader().getShapeType() != ShapeType.POLYGON_Z && reader.getHeader().getShapeType() != ShapeType.POLYGON_M){
            throw new InvalidMapException("Seuls les polygones sont gérés pour les régions");
        }
        return true;
    }


    /**
     * Retourne la taille en X de la carte
     * @return Taille en X
     */
    public double getMapSizeX() {
        return (Math.abs(_reader.getHeader().getBoxMaxX()) - _reader.getHeader().getBoxMinX());
    }

    /**
     * Retourne la taille en Y de la carte
     * @return Taille en Y
     */
    public double getMapSizeY() {
        return (Math.abs(_reader.getHeader().getBoxMaxY()) - _reader.getHeader().getBoxMinY());
    }

    /**
     * Retourne la valeur minimale de X
     * @return Minimum de X
     */
    public double getMapMinX() {
        return _reader.getHeader().getBoxMinX();
    }

    /**
     * Retourne la valeur minimale de Y
     * @return Minimum de Y
     */
    public double getMapMinY() {
        return _reader.getHeader().getBoxMinY();
    }

    /**
     * Retourne la valeur maximale de X
     * @return Maximum de X
     */
    public double getMapMaxX() {
        return _reader.getHeader().getBoxMaxX();
    }

    /**
     * Retourne la valeur maximale de Y
     * @return Maximum de Y
     */
    public double getMapMaxY() {
        return _reader.getHeader().getBoxMaxY();
    }

    /**
     * Retourne le type de formes contenues dans le fichier shapefile
     * @return Type des formes
     */
    public String getShapeName() {
        return _reader.getHeader().getShapeType().name();
    }

    /**
     * Affiche les infos globales sur le fichier dans la console
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

    /**
     * Retourne la prochaine forme contenue dans le flux de données
     * @return Polygone JavaFX contenant les frontières de la région
     * @throws IOException
     * @throws InvalidShapeFileException
     */
    /*
    public List<Polygon> getNextShape() throws IOException, InvalidShapeFileException {
        AbstractShape shape = _reader.next();
        List<Polygon> polygons = new ArrayList<>();
        if (shape != null)
        {
            AbstractPolyShape polygonShape = (AbstractPolyShape) shape;
            for (int i = 0; i < polygonShape.getNumberOfParts(); i++) {
                polygons.add(addPointDataArrayToPolygon(new Polygon(),polygonShape.getPointsOfPart(i)));
            }
        }
        return polygons;
    }

    private Polygon addPointDataArrayToPolygon(Polygon polygon, PointData[] points) {
        for (PointData point : points) {
            polygon.getPoints().add(point.getX() - getMapMinX());
            polygon.getPoints().add(point.getY() - getMapMinY());
        }
        return polygon;
    }
    */
    
    public List<RawPolygon> getNextShape() throws IOException, InvalidShapeFileException {
        AbstractShape shape = _reader.next();
        List<RawPolygon> polygons = new ArrayList<>();
        if (shape != null)
        {
            AbstractPolyShape polygonShape = (AbstractPolyShape) shape;
            for (int i = 0; i < polygonShape.getNumberOfParts(); i++) {
                polygons.add(addPointDataArrayToPolygon(polygonShape.getPointsOfPart(i)));
            }
        }
        return polygons;
    }

    private RawPolygon addPointDataArrayToPolygon(PointData[] points) {
        RawPolygon polygon = new RawPolygon();
        for (PointData point : points) {
            polygon.getPoints().add(new Point(point.getX() - getMapMinX(),point.getY() - getMapMinY()));
        }
        return polygon;
    }
}
