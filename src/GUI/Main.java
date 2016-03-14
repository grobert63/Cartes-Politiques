package GUI;

import Entities.GeoMap;
import Entities.HexGrid;
import Loader.MapLoader;
import LoggerUtils.LoggerManager;
import Resolver.Test2Resolver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.logging.Level;


public class Main extends Application {
    public static HexGrid grid;
    public static GeoMap geoMap;
    public static ArrayList<String> nameColumns = new ArrayList<>();


    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        LoggerManager.getInstance().getLogger().log(Level.INFO, "Starting...");
        Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
        primaryStage.setTitle("Hexagomap");
        primaryStage.setScene(new Scene(root, 650, 400));
        primaryStage.show();
        LoggerManager.getInstance().getLogger().log(Level.INFO, "Application started");
    }

    public static void chargement(String shp, String dbf) throws Exception {
        // Chargement des régions en mémoire
        MapLoader ml;
        if (shp == null) {
            ml = new MapLoader(
                    "test/FRA_adm1.shp",
                    "test/FRA_adm1.dbf"
                    //"test/world.shp",
                    //"test/world.dbf"
                    //"test/usstate500k.shp",
                    //"test/usstate500k.dbf"
                    //"test/usstate20m.shp",
                    //"test/usstate20m.dbf"
            );
        } else {
            ml = new MapLoader(shp, dbf);
        }


        geoMap = ml.load();

        // Le champ par défaut correspond au nom de la colonne contenant le nom de la région dans le .dbf
        int i;
        nameColumns.clear();

        if (ml.getDbfReader() != null) {
            for (i = 0; i < ml.getDbfReader().getFieldCount(); ++i) {
                if (ml.getDbfReader().getField(i).getName().toLowerCase().contains("name"))
                    nameColumns.add(ml.getDbfReader().getField(i).getName());
            }
            geoMap.getManager().setRegionsName(nameColumns.get(nameColumns.size() - 1));
        }

        Test2Resolver algo = new Test2Resolver();
        grid = algo.resolve(geoMap.getRegions(), 0, true, geoMap.getRegions().get(0), -1);
    }
}
