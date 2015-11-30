package GUI;

import Entities.HexGrid;
import Entities.Map;
import Entities.Region;
import Loader.MapLoader;
import Resolver.IResolver;
import Resolver.SimpleAggregerResolver;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class Main extends Application {
    public static HexGrid grid;
    public static Map map;
    
    public static void main(String[] args) throws Exception {
        chargement();
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
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
        
        map = ml.load(0.5);
        
        for(Region r : map.getRegions()){
            // Le champ par défaut correspond au nom de la colonne contenant le nom de la région dans le .dbf
            r.setDefaultField("NAME_1");
            //r.setDefaultField("name");
            //r.setDefaultField("NAME");
            afficherRegion(r);
        }
        
        // Placement aléatoire des régions sur la grille hexagonale pour les tests.
        IResolver algo = new SimpleAggregerResolver();
        grid = algo.resolve(map.getRegions());
    }
    
    public static void afficherRegion(Region r){
        System.out.println("<"+r.getName()+">");
        System.out.println("\tCentreX : "+r.getCenterX());
        System.out.println("\tCentreY : "+r.getCenterY());
    }
}
