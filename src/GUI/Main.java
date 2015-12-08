package GUI;

import DataManager.Load;
import Entities.HexGrid;
import Entities.Map;
import Entities.Region;
import Loader.MapLoader;
import LoggerUtils.LoggerManager;
import Resolver.IResolver;
import Resolver.SimpleAggregerResolver;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;


public class Main extends Application {
    public static HexGrid grid;
    public static Map map;
    
    public static void main(String[] args) throws Exception {
        chargement();
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        LoggerManager.getInstance().getLogger().log(Level.INFO,"Starting...");
        HBox root = new HBox();
        // Création et affichage de la grille hexagonale
        Canvas hexgrid = new HexCanvas(800, 700, grid);
        root.getChildren().add(hexgrid);
        Canvas polygrid = new PolyCanvas(800, 700, map);
        root.getChildren().add(polygrid);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Canvas Test");
        stage.show();
        try {
            Load.loadMultiple(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void chargement() throws Exception {   
        // Chargement des régions en mémoire
        MapLoader ml = new MapLoader(
                "test/FRA_adm1.shp",
                "test/FRA_adm1.dbf"
                //"test/world.shp",
                //"test/world.dbf"
                //"test/usstate500k.shp",
                //"test/usstate500k.dbf"
                //"test/usstate20m.shp",
                //"test/usstate20m.dbf"
        );
        
        map = ml.load();
        
        for(Region r : map.getRegions()){
            // Le champ par défaut correspond au nom de la colonne contenant le nom de la région dans le .dbf
            r.setDefaultField("NAME_1");
            //r.setDefaultField("name");
            //r.setDefaultField("NAME");
            //afficherRegion(r);
        }
        
        //debug(map.getRegions());
        IResolver algo = new SimpleAggregerResolver();
        grid = algo.resolve(map.getRegions());
    }
    
    public static void afficherRegion(Region r){
        System.out.println("<"+r.getName()+">");
        System.out.println("\tCentreX : "+r.getCenter().x);
        System.out.println("\tCentreY : "+r.getCenter().y);
    }
    
    public static void debug(List<Region> l){
        int nbRegions = map.getRegions().size();
        Region a,b;
        for(int i=0;i<nbRegions;i++){
            a = map.getRegions().get(i);
            for(int j=0;j<nbRegions;j++){
                b =  map.getRegions().get(j);
                afficherFrontiere(a,b,a.pourcentageDeFrontiereCommune(b));
            }
        }
    }
    
    public static void afficherFrontiere(Region a, Region b, double pourcentage){
        System.out.println("<"+a.getName()+">");
        System.out.println("<"+b.getName()+">");
        System.out.println("\tPourcentage : "+(int)(pourcentage*100)+" %");
        System.out.println();
    }
}
