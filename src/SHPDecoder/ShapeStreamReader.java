//Attention grosses modifications en cours, ne pas utiliser
package SHPDecoder;

import exception.InvalidMapException;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;

import java.io.FileInputStream;
import java.io.IOException;

public class ShapeStreamReader {
    private ShapeFileReader reader = null;
    private double zoom = 1;
    private static final double ZOOM_MIN = 1;
    private static final double ZOOM_MAX = 100;
    private boolean drawn = false;

    public ShapeStreamReader(FileInputStream stream) throws InvalidShapeFileException, IOException, InvalidMapException {
        if (stream != null) {
            try {
                reader = new ShapeFileReader(stream);
            } catch (InvalidShapeFileException e) {
                throw new InvalidShapeFileException("La forme est invalide",e.getCause());
            } catch (IOException e) {
                throw new IOException("Une erreur de lecture est survenue",e.getCause());
            }
            if(reader.getHeader().getBoxMaxZ() != 0 || reader.getHeader().getBoxMinZ() != 0 || reader.getHeader().getBoxMaxM() != 0 || reader.getHeader().getBoxMinM() != 0)
            {
                throw new InvalidMapException("Seules les cartes 2D sont gérées");
            }
        }
        else
        {
            throw new NullPointerException("Le flux de données est absent");
        }
    }

    public double getMapSizeX()
    {
        return (Math.abs(reader.getHeader().getBoxMaxX()) - reader.getHeader().getBoxMinX()) * zoom;
    }

    public double getMapSizeY()
    {
        return (Math.abs(reader.getHeader().getBoxMaxY()) - reader.getHeader().getBoxMinY()) * zoom;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        if (zoom >= ZOOM_MIN && zoom <= ZOOM_MAX)
            this.zoom = zoom;
    }

    public double getMapMinX()
    {
        return reader.getHeader().getBoxMinX() * zoom;
    }

    public double getMapMinY()
    {
        return reader.getHeader().getBoxMinY() * zoom;
    }

    public double getMapMaxX()
    {
        return reader.getHeader().getBoxMaxX() * zoom;
    }

    public double getMapMaxY()
    {
        return reader.getHeader().getBoxMaxY() * zoom;
    }

    public String getShapeName()
    {
        return reader.getHeader().getShapeType().name();
    }

    public void printInfos()
    {
        System.out.println("min (X) : " + getMapMinX());
        System.out.println("max (X) : " + getMapMaxX());
        System.out.println("size (X) : " + getMapSizeX());
        System.out.println("min (Y) : " + getMapMinY());
        System.out.println("max (Y) : " + getMapMaxY());
        System.out.println("size (Y) : " + getMapSizeY());
        System.out.println("Zoom : " + getZoom());
        System.out.println("Shape File : " + getShapeName());
    }

    public void drawMap(Group root) throws InvalidShapeFileException, IOException
    {
        if (!drawn) {
            Canvas canvas = new Canvas(getMapSizeX(), getMapSizeY());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            AbstractShape shape;
            PointData previous = null;
            while ((shape = reader.next()) != null) {
                PolygonShape polygon = (PolygonShape) shape;
                for (int i = 0; i < polygon.getNumberOfParts(); i++) {
                    PointData[] points = polygon.getPointsOfPart(i);
                    for (PointData point : points) {
                        if (previous != null) {
                            gc.strokeLine(previous.getX() * zoom - getMapMinX(), getMapSizeY() - (previous.getY() * zoom - getMapMinY()), point.getX() * zoom - getMapMinX(), getMapSizeY() - (point.getY() * zoom - getMapMinY()));
                        }
                        previous = point;
                    }
                    previous = null;
                }

            }
            root.getChildren().add(canvas);
            drawn = true;
        }
    }
}
