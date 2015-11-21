package SHPDecoder;

import exception.InvalidMapException;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Polygon;
import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;

import java.io.FileInputStream;
import java.io.IOException;

public class ShapeStreamReader {
    private ShapeFileReader reader = null;

    private ObservableList<Polygon> listPolygon = FXCollections.observableArrayList();

    private final ListProperty<Polygon> ListPolygonProperty = new SimpleListProperty<>(listPolygon);
        public final ListProperty<Polygon> ListPolygonProperty() {
        return ListPolygonProperty;
        }
        public final ObservableList<Polygon> getListPolygonProperty() {
            return ListPolygonProperty.get();
        }
        public final void setListPolygonProperty(ObservableList<Polygon> list) {
            ListPolygonProperty.set(list);
    }

    /*private double zoom = 1;
    private static final double ZOOM_MIN = 0.1;
    private static final double ZOOM_MAX = 100;*/

    public ShapeStreamReader(FileInputStream stream) throws InvalidShapeFileException, IOException, InvalidMapException {
        if (stream != null) {
            createShapeFileReader(stream);
            retrievePolygons();
        } else {
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
        if (reader.getHeader().getBoxMaxZ() != 0 || reader.getHeader().getBoxMinZ() != 0 || reader.getHeader().getBoxMaxM() != 0 || reader.getHeader().getBoxMinM() != 0)
            throw new InvalidMapException("Seules les cartes 2D sont gérées");
    }


    public double getMapSizeX() {
        return (Math.abs(reader.getHeader().getBoxMaxX()) - reader.getHeader().getBoxMinX());
    }

    public double getMapSizeY() {
        return (Math.abs(reader.getHeader().getBoxMaxY()) - reader.getHeader().getBoxMinY());
    }

    /*public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        if (zoom >= ZOOM_MIN && zoom <= ZOOM_MAX)
            this.zoom = zoom;
    }*/

    public double getMapMinX() {
        return reader.getHeader().getBoxMinX();
    }

    public double getMapMinY() {
        return reader.getHeader().getBoxMinY();
    }

    public double getMapMaxX() {
        return reader.getHeader().getBoxMaxX();
    }

    public double getMapMaxY() {
        return reader.getHeader().getBoxMaxY();
    }

    public String getShapeName() {
        return reader.getHeader().getShapeType().name();
    }

    public void printInfos() {
        System.out.println("min (X) : " + getMapMinX());
        System.out.println("max (X) : " + getMapMaxX());
        System.out.println("size (X) : " + getMapSizeX());
        System.out.println("min (Y) : " + getMapMinY());
        System.out.println("max (Y) : " + getMapMaxY());
        System.out.println("size (Y) : " + getMapSizeY());
        System.out.println("Shape File : " + getShapeName());
    }

    private void retrievePolygons() throws IOException, InvalidShapeFileException {
        AbstractShape shape;
        while ((shape = reader.next()) != null) {
            PolygonShape polygonShape = (PolygonShape) shape;
            for (int i = 0; i < polygonShape.getNumberOfParts(); i++) {
                Polygon polygon = createPolygonFromPointDataArray(polygonShape.getPointsOfPart(i));
                listPolygon.add(polygon);
            }
        }
    }

    private Polygon createPolygonFromPointDataArray(PointData[] points) {
        Polygon polygon = new Polygon();
        for (PointData point : points) {
            polygon.getPoints().add(point.getX() - getMapMinX());
            polygon.getPoints().add(getMapSizeY() - (point.getY() - getMapMinY()));
        }
        return polygon;
    }
}
