package GUI;

import DataManager.Load;
import GUI.*;
import GUI.Main;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class Controller {

    private PolyCanvas canvasCarte;
    private HexCanvas canvas;

    @FXML
    Pane PaneAffichageResult;

    @FXML
    ScrollBar ScrollHResult;

    @FXML
    ScrollBar ScrollVResult;

    @FXML
    Slider SliderResult;

    @FXML
    Pane PaneAffichageCarte;

    @FXML
    ScrollBar ScrollHCarte;

    @FXML
    ScrollBar ScrollVCarte;

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

        ScrollHCarte.setValue(50);
        ScrollVCarte.setValue(50);

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

        canvasCarte.zoomProperty().bind(SliderZoomCarte.valueProperty());
        canvas.zoomProperty().bind(SliderResult.valueProperty());

        canvas.decalageXProperty().bind(ScrollHResult.valueProperty().multiply(-1).multiply(canvas.widthProperty().divide(ScrollHResult.maxProperty())));
        canvasCarte.decalageXProperty().bind(ScrollHCarte.valueProperty().subtract(50).multiply(-1).multiply(canvasCarte.widthProperty().divide(ScrollHCarte.maxProperty())));
        canvas.decalageYProperty().bind(ScrollVResult.valueProperty().multiply(-1).multiply(canvas.heightProperty().divide(ScrollHResult.maxProperty())));
        canvasCarte.decalageYProperty().bind(ScrollVCarte.valueProperty().subtract(50).multiply(-1).multiply(canvasCarte.heightProperty().divide(ScrollVCarte.maxProperty())));

        canvasCarte.nomPaysProperty().bind(NomPaysCarte.selectedProperty());
    }


    @FXML
    public void load()
    {
        try {
            File dbf = null;
            File shp = Load.loadSingle(fenetre.getScene().getWindow(),new FileChooser.ExtensionFilter("Shapefile", "*.shp"),new File(System.getProperty("user.home")));
            if(shp != null ) dbf = Load.loadSingle(fenetre.getScene().getWindow(),new FileChooser.ExtensionFilter("DataBase File", "*.dbf"),shp.getParentFile());
            if(dbf != null) Main.chargement(shp.getPath(),dbf.getPath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
