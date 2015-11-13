package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.ValidationPreferences;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.header.ShapeFileHeader;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.ShapeType;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;

import java.io.*;

public class Main extends Application {

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
        double x = Math.abs(reader.getHeader().getBoxMaxX()) + Math.abs(reader.getHeader().getBoxMinX());
        double y = Math.abs(reader.getHeader().getBoxMaxY()) + Math.abs(reader.getHeader().getBoxMinY());
        double minX = reader.getHeader().getBoxMinX();
        double minY = reader.getHeader().getBoxMinY();
        System.out.println("min (X) : " + minX);
        System.out.println("max (X) : " + reader.getHeader().getBoxMaxX());
        System.out.println("size (X) : " + x);
        System.out.println("min (Y) : " + minY);
        System.out.println("max (Y) : " + reader.getHeader().getBoxMaxY());
        System.out.println("size (Y) : " + y);
        if((Math.abs(reader.getHeader().getBoxMaxZ()) + Math.abs(reader.getHeader().getBoxMinZ())) != 0 || (Math.abs(reader.getHeader().getBoxMaxM()) + Math.abs(reader.getHeader().getBoxMinM())) != 0){
            System.out.println("Format de carte invalide");
            System.exit(4);
        }


        Group root = new Group();
        Canvas canvas = new Canvas(x, y);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFill(Color.BLUE);
        System.out.println("Shape File : " + reader.getHeader().getShapeType().name());

        AbstractShape shape = null;
        int nb = 0;
        PointData previous = null;
        while ((shape = reader.next()) != null)
        {
            PolygonShape polygon = (PolygonShape) shape;
            /*System.out.println("I read a Polygon with "
                    + polygon.getNumberOfParts() + " parts and "
                    + polygon.getNumberOfPoints() + " points");*/
            for (int i = 0; i < polygon.getNumberOfParts(); i++) {
                PointData[] points = polygon.getPointsOfPart(i);
                /*System.out.println("- part " + i + " has " + points.length
                        + " points.");*/
                for(PointData point : points)
                {
                    if(previous != null)
                    {
                        gc.strokeLine(previous.getX()+Math.abs(minX),previous.getY()+Math.abs(minY),point.getX()+Math.abs(minX),point.getY()+Math.abs(minY));
                    }
                    previous = point;
                }
                previous = null;
            }
            nb++;
            //System.out.println(shape.getHeader().getContentLength());
            //System.out.println(shape.getHeader().getRecordNumber());
            //System.out.println(shape.getHeader().getContentLength());

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
