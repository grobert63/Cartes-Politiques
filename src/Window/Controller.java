package Window;

import GUI.*;
import GUI.Main;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class Controller {

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
    Slider SliderCarte;

    @FXML
    CheckBox NomPaysCarte;
    @FXML
    void initialize()
    {
        SliderResult.setMin(1);
        SliderResult.setMax(10);
        SliderResult.setBlockIncrement(0.01);

        SliderCarte.setMin(1);
        SliderCarte.setMax(10);
        SliderCarte.setBlockIncrement(0.01);
        
        ScrollHCarte.setValue(50);
        ScrollVCarte.setValue(50);

        try {
            GUI.Main.chargement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PolyCanvas canvasCarte = new PolyCanvas( Main.map);
        HexCanvas canvas = new HexCanvas(1000, 700,GUI.Main.grid);

        PaneAffichageCarte.getChildren().add(canvasCarte);
        PaneAffichageResult.getChildren().add(canvas);


        canvas.heightProperty().bind(PaneAffichageResult.heightProperty());
        canvas.widthProperty().bind(PaneAffichageResult.widthProperty());
        canvasCarte.widthProperty().bind(PaneAffichageCarte.widthProperty());
        canvasCarte.heightProperty().bind(PaneAffichageCarte.heightProperty());

        canvasCarte.zoomProperty().bind(SliderCarte.valueProperty());
        canvas.zoomProperty().bind(SliderResult.valueProperty());

        canvas.decalageXProperty().bind(ScrollHResult.valueProperty().multiply(-1).multiply(canvas.widthProperty().divide(ScrollHResult.maxProperty())));
        canvasCarte.decalageXProperty().bind(ScrollHCarte.valueProperty().subtract(50).multiply(-1).multiply(canvasCarte.widthProperty().divide(ScrollHCarte.maxProperty())));
        canvas.decalageYProperty().bind(ScrollVResult.valueProperty().multiply(-1).multiply(canvas.heightProperty().divide(ScrollHResult.maxProperty())));
        canvasCarte.decalageYProperty().bind(ScrollVCarte.valueProperty().subtract(50).multiply(-1).multiply(canvasCarte.heightProperty().divide(ScrollVCarte.maxProperty())));

        canvasCarte.nomPaysProperty().bind(NomPaysCarte.selectedProperty());
    }
}
