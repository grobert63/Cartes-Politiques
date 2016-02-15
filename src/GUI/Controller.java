package GUI;

import DataManager.Converter;
import DataManager.Load;
import DataManager.Save;
import GUI.*;
import GUI.Main;
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

public class Controller {

    public Menu menuName;
    private PolyCanvas canvasCarte;
    private HexCanvas canvas;

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
                canvasCarte.draw();
            });
            menuName.getItems().add(radio);

        }

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
}
