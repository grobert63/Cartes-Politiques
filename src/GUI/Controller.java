package GUI;

import DataManager.Converter;
import DataManager.Load;
import DataManager.Save;
import Entities.Direction;
import Entities.Region;
import GUI.*;
import GUI.Main;
import Resolver.Test2Resolver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Controller {

    public Menu menuName;
    private PolyCanvas canvasCarte;
    private HexCanvas canvas;

    private String FirstRegion;
    private int Direction;
    private boolean Clock;
    private int nbTour;

    @FXML
    Pane PaneAffichageResult;


    @FXML
    Slider SliderResult;

    @FXML
    Pane PaneAffichageCarte;

    @FXML
    Slider SliderZoomCarte;
 
    @FXML
    CheckBox NomPaysCarte;

    @FXML
    BorderPane fenetre;

    @FXML
    Menu FirstCountryArgument;

    @FXML
    Menu  MenuDirection;

    @FXML
    Menu occurenceMenu;

    @FXML
    void initialize()
    {
        SliderResult.setMin(1);
        SliderResult.setMax(10);
        SliderResult.setBlockIncrement(0.01);

        SliderZoomCarte.setMin(1);
        SliderZoomCarte.setMax(10);
        SliderZoomCarte.setBlockIncrement(0.01);

        try {
            Main.chargement(null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        canvasCarte = new PolyCanvas( Main.geoMap);
        canvas = new HexCanvas(1000, 700,GUI.Main.grid);

        PaneAffichageCarte.getChildren().add(canvasCarte);
        PaneAffichageResult.getChildren().add(canvas);


        canvas.heightProperty().bind(PaneAffichageResult.heightProperty());
        canvas.widthProperty().bind(PaneAffichageResult.widthProperty());
        canvasCarte.widthProperty().bind(PaneAffichageCarte.widthProperty());
        canvasCarte.heightProperty().bind(PaneAffichageCarte.heightProperty());

        canvasCarte.zoomProperty().bindBidirectional(SliderZoomCarte.valueProperty());
        canvas.zoomProperty().bindBidirectional(SliderResult.valueProperty());


        canvasCarte.nomPaysProperty().bind(NomPaysCarte.selectedProperty());


        chargementName();
        chargementArgs();
    }

    private void chargementName()
    {
        menuName.getItems().clear();
        ToggleGroup toggleGroup = new ToggleGroup();
        for (String name :Main.nameColumns) {
            RadioMenuItem radio = new RadioMenuItem(name);
            radio.setToggleGroup(toggleGroup);
            radio.setSelected(true);
            radio.setOnAction(event -> {
                Main.geoMap.debug_getManager().setRegionsName(radio.getText());
                canvas.draw();
                chargementArgumentName();
                canvasCarte.draw();
            });
            menuName.getItems().add(radio);

        }

    }

    private void chargementArgumentName()
    {
        FirstCountryArgument.getItems().clear();
        ToggleGroup toggleGroup = new ToggleGroup();
        int first = 0;
        for (Region r:Main.geoMap.getRegions()) {
            RadioMenuItem radio = new RadioMenuItem(r.getName());
            radio.setToggleGroup(toggleGroup);
            if(first == 0){
                radio.setSelected(true);
                FirstRegion = r.getName();
            }
            radio.setOnAction(event -> FirstRegion = r.getName());
            FirstCountryArgument.getItems().add(radio);
            first++;
        }

    }




    private void chargementArgs()
    {
        Direction = Entities.Direction.NORTH_EAST;
        Clock = true;
        nbTour = -1;
        ToggleGroup toggleGroup = new ToggleGroup();
        MenuDirection.getItems().clear();
        addRadio(MenuDirection,toggleGroup,"North-East",event -> Direction = Entities.Direction.NORTH_EAST,true);
        addRadio(MenuDirection,toggleGroup,"East",event -> Direction = Entities.Direction.EAST,false);
        addRadio(MenuDirection,toggleGroup,"South-East",event -> Direction = Entities.Direction.SOUTH_EAST,false);
        addRadio(MenuDirection,toggleGroup,"South-West",event -> Direction = Entities.Direction.SOUTH_WEST,false);
        addRadio(MenuDirection,toggleGroup,"West",event -> Direction = Entities.Direction.WEST,false);
        addRadio(MenuDirection,toggleGroup,"North-West",event -> Direction = Entities.Direction.NORTH_WEST,false);
        MenuDirection.getItems().add(new SeparatorMenuItem());

        ToggleGroup toggleGroupClock = new ToggleGroup();
        addRadio(MenuDirection,toggleGroupClock,"ClockWork",event -> Clock = true,true);
        addRadio(MenuDirection,toggleGroupClock,"Counter ClockWork",event -> Clock = false,false);

        ToggleGroup toggleOccu = new ToggleGroup();
        occurenceMenu.getItems().clear();
        addRadio(occurenceMenu,toggleOccu,"All",event -> nbTour = -1,true);
        addRadio(occurenceMenu,toggleOccu,"1",event -> nbTour = 1,false);
        addRadio(occurenceMenu,toggleOccu,"2",event -> nbTour = 2,false);
        addRadio(occurenceMenu,toggleOccu,"3",event -> nbTour = 3,false);
        addRadio(occurenceMenu,toggleOccu,"4",event -> nbTour = 4,false);
        addRadio(occurenceMenu,toggleOccu,"5",event -> nbTour = 5,false);
        addRadio(occurenceMenu,toggleOccu,"6",event -> nbTour = 6,false);
        addRadio(occurenceMenu,toggleOccu,"7",event -> nbTour = 7,false);
        addRadio(occurenceMenu,toggleOccu,"8",event -> nbTour = 8,false);
        addRadio(occurenceMenu,toggleOccu,"9",event -> nbTour = 9,false);
        addRadio(occurenceMenu,toggleOccu,"10",event -> nbTour = 10,false);

        chargementArgumentName();

    }

    private void addRadio(Menu menu,ToggleGroup toggle,String name,EventHandler handler,boolean selected)
    {
        RadioMenuItem radio= new RadioMenuItem(name);
        radio.setToggleGroup(toggle);
        radio.setSelected(selected);
        radio.setOnAction(handler);
        menu.getItems().add(radio);
    }

    @FXML
    public void loadDbf()
    {
        try {
            File dbf = null;
            File shp = Load.loadSingle(fenetre.getScene().getWindow(),new FileChooser.ExtensionFilter("Shapefile", "*.shp"),new File(System.getProperty("user.home")));
            if(shp != null ) dbf = Load.loadSingle(fenetre.getScene().getWindow(),new FileChooser.ExtensionFilter("DataBase File", "*.dbf"),shp.getParentFile());
            if(dbf != null) Main.chargement(shp.getPath(),dbf.getPath());

        } catch (Exception e) {
            e.printStackTrace();
        }
        chargementName();
        chargementArgumentName();
        canvas.initialize();
        canvas.draw();
        canvasCarte.initialize();
        canvasCarte.draw();
    }

    @FXML
    public void load()
    {
        try {
            File shp = Load.loadSingle(fenetre.getScene().getWindow(),new FileChooser.ExtensionFilter("Shapefile", "*.shp"),new File(System.getProperty("user.home")));
            if(shp != null) Main.chargement(shp.getPath(),null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        chargementName();
        chargementArgumentName();
        canvas.initialize();
        canvas.draw();
        canvasCarte.initialize();
        canvasCarte.draw();
    }

    @FXML
    public void SaveImage()
    {
        Save.saveToImage(fenetre.getScene().getWindow(), Converter.CanvasToImage(canvas));
    }

    @FXML
    public void refreshAlgo()
    {
        Test2Resolver algo = new Test2Resolver();
        Region region = null;
        for (Region r: Main.geoMap.getRegions()) {
            if(Objects.equals(r.getName(), FirstRegion)) region = r;
        }
        if(region != null) {
            Main.grid = algo.resolve(Main.geoMap.getRegions(), Direction, Clock, region, nbTour);
            canvas.draw();
        }
    }
}
