package SHPDecoder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        FileInputStream fileInputStream = null;
        try
        {
            fileInputStream = new FileReader("test/world.shp").getFileInputStream();
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
        reader.printInfos();

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 650, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

    }
}
