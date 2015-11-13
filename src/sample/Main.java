package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    private static final double ZOOM = 5.4;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(root, 300, 275));


        File file = new File("C:/Users/supra63200/IdeaProjects/Cartes-Politiques/test/world.shp");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            System.out.println("Total file size to read (in bytes) : "
                    + fileInputStream.available());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
        ShapeFileReader reader = null;
        try {
             reader = new ShapeFileReader(fileInputStream);
        }
        catch (InvalidShapeFileException e)
        {
            e.printStackTrace();
            System.exit(3);
        }
        double x = (Math.abs(reader.getHeader().getBoxMaxX()) - reader.getHeader().getBoxMinX()) * ZOOM;
        double y = (Math.abs(reader.getHeader().getBoxMaxY()) - reader.getHeader().getBoxMinY()) * ZOOM;
        double minX = reader.getHeader().getBoxMinX() * ZOOM;
        double minY = reader.getHeader().getBoxMinY() * ZOOM;
        System.out.println("min (X) : " + minX);
        System.out.println("max (X) : " + reader.getHeader().getBoxMaxX() * ZOOM);
        System.out.println("size (X) : " + x);
        System.out.println("min (Y) : " + minY);
        System.out.println("max (Y) : " + reader.getHeader().getBoxMaxY() * ZOOM);
        System.out.println("size (Y) : " + y);
        if(reader.getHeader().getBoxMaxZ() != 0 || reader.getHeader().getBoxMinZ() != 0 || reader.getHeader().getBoxMaxM() != 0 || reader.getHeader().getBoxMinM() != 0){
            System.out.println("Format de carte invalide");
            System.exit(4);
        }


        Group root = new Group();
        Canvas canvas = new Canvas(x, y);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        System.out.println("Shape File : " + reader.getHeader().getShapeType().name());

        AbstractShape shape;
        int nb = 0;
        PointData previous = null;
        while ((shape = reader.next()) != null)
        {
            PolygonShape polygon = (PolygonShape) shape;
            for (int i = 0; i < polygon.getNumberOfParts(); i++) {
                PointData[] points = polygon.getPointsOfPart(i);
                for(PointData point : points)
                {
                    if(previous != null)
                    {
                        gc.strokeLine(previous.getX() * ZOOM - minX, y - (previous.getY() * ZOOM - minY), point.getX() * ZOOM - minX,y - (point.getY() * ZOOM - minY));
                    }
                    previous = point;
                }
                previous = null;
            }
            nb++;

        }
        System.out.println("nb :" + nb);

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);

    }
}
