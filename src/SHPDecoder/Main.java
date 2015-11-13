package SHPDecoder;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(root, 300, 275));

        FileInputStream fileInputStream = null;
        try
        {
            fileInputStream = new FileReader("C:/Users/supra63200/IdeaProjects/Cartes-Politiques/test/world.shp").getFileInputStream();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        ShapeStreamReader reader = null;
        try {
            reader = new ShapeStreamReader(fileInputStream);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        Group root = new Group();
        reader.printInfos();
        reader.setZoom(5);
        reader.drawMap(root);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        System.out.println();
        System.out.println();
        reader.printInfos();
    }


    public static void main(String[] args) {
        launch(args);

    }
}
