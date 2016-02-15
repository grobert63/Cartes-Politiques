package GUI;

import DataManager.Converter;
import DataManager.Save;
import Debug.TimeDebug;
import Entities.Boundary;
import Entities.HexGrid;
import Entities.GeoMap;
import Entities.Region;
import Loader.MapLoader;
import LoggerUtils.LoggerManager;
import Resolver.IResolver;
import Resolver.SimpleAggregerResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.hexiong.jdbf.DBFReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 650, 400));
        primaryStage.show();
        LoggerManager.getInstance().getLogger().log(Level.INFO, "Application started");
    }

    public static void chargement(String shp,String dbf) throws Exception {
        TimeDebug.timeStart(0);
        // Chargement des régions en mémoire
        MapLoader ml;
        if(shp == null)
        {
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
        }
        else
        {
            ml = new MapLoader(shp,dbf);
        }


        geoMap = ml.load();
        
        // Le champ par défaut correspond au nom de la colonne contenant le nom de la région dans le .dbf
        int i;
        nameCollumns.clear();
        if(ml.getDbfReader() != null) {
            for (i = 0; i < ml.getDbfReader().getFieldCount(); ++i) {
                if (ml.getDbfReader().getField(i).getName().toLowerCase().contains("name"))
                    nameCollumns.add(ml.getDbfReader().getField(i).getName());
            }
            geoMap.debug_getManager().setRegionsName(nameCollumns.get(nameCollumns.size() - 1));
        }
        //geoMap.debug_getManager().setRegionsName("name");
        //geoMap.debug_getManager().setRegionsName("NAME");

        geoMap.getRegions().forEach(Main::afficherRegion);

        TimeDebug.timeStart(21);
        IResolver algo = new SimpleAggregerResolver();
        grid = algo.resolve(geoMap.getRegions());
        TimeDebug.timeStop(21);
        
        TimeDebug.timeStop(0);
        
        TimeDebug.setTimeLabel(0, "Temps de chargement total de la carte");
        TimeDebug.displayTime(0);
        
        TimeDebug.setTimeLabel(18, "Chargement à partir des fichiers");
        TimeDebug.displayPourcentage(18, 0);
        
        TimeDebug.setTimeLabel(19, "Topologisation");
        TimeDebug.displayPourcentage(19, 0);
        
        TimeDebug.setTimeLabel(1, "Simplification de la carte");
        TimeDebug.displayPourcentage(1, 0);
        
        TimeDebug.setTimeLabel(20, "Calcul des voisins");
        TimeDebug.displayPourcentage(20, 0);
        
        TimeDebug.setTimeLabel(21, "Algorithme de résolution \""+algo.getClass().getSimpleName()+'"');
        TimeDebug.displayPourcentage(21, 0);
    }
    
    public static void afficherRegion(Region r){
        System.out.println("<"+r.getName()+">");
        Map<Region,List<Boundary>> neighbors = r.getNeighbors();
        for(Entry<Region,List<Boundary>> e : neighbors.entrySet()){
            Region neighbor = e.getKey();
            List<Boundary> boundary = e.getValue();
            if(neighbor != null){
                System.out.println("\t"+neighbor.getName()+" : "+boundary.size());
            }
            else{
                System.out.println("\t *Vide* : "+boundary.size());
            }
        }
        
        //System.out.println("\tCentreX : "+r.getCenter().x);
        //System.out.println("\tCentreY : "+r.getCenter().y);
    }

    /*
    @Override
    public void start(Stage stage) {
        LoggerManager.getInstance().getLogger().log(Level.INFO, "Starting...");
        HBox root = new HBox();
        // Création et affichage de la grille hexagonale
        Canvas hexgrid = new HexCanvas(800, 700, grid);
        root.getChildren().add(hexgrid);
        Canvas polygrid = new PolyCanvas(geoMap);
        root.getChildren().add(polygrid);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Canvas Test");
        stage.show();
        Save.saveToImage(stage, Converter.CanvasToImage(hexgrid));
        //try {
        //    Load.loadMultiple(stage);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        
    }
    */
}
