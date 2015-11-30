package Window;

import GUI.*;
import GUI.Main;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
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
    void initialize()
    {
        SliderResult.setMin(1);
        SliderResult.setMax(2);
        SliderResult.setBlockIncrement(0.01);

        SliderCarte.setMin(1);
        SliderCarte.setMax(2);
        SliderCarte.setBlockIncrement(0.01);

        try {
            GUI.Main.chargement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PolyCanvas canvasCarte = new PolyCanvas(1000,700, Main.map);
        HexCanvas canvas = new HexCanvas(1000, 700,GUI.Main.grid);

        PaneAffichageCarte.getChildren().add(canvasCarte);
        PaneAffichageResult.getChildren().add(canvas);

        canvas.widthProperty().bind(PaneAffichageResult.widthProperty());
        canvas.heightProperty().bind(PaneAffichageResult.heightProperty());
        canvasCarte.widthProperty().bind(PaneAffichageCarte.widthProperty());
        canvasCarte.heightProperty().bind(PaneAffichageCarte.heightProperty());

        canvasCarte.zoomProperty().bind(SliderCarte.valueProperty());
        canvas.zoomProperty().bind(SliderResult.valueProperty());

        canvas.decalageXProperty().bind(ScrollHResult.valueProperty().multiply(-1).multiply(canvas.widthProperty().divide(ScrollHResult.maxProperty())));
        canvasCarte.decalageXProperty().bind(ScrollHCarte.valueProperty().multiply(-1).multiply(canvas.widthProperty().divide(ScrollHCarte.maxProperty())));
        canvas.decalageYProperty().bind(ScrollVResult.valueProperty().multiply(-1).multiply(canvas.widthProperty().divide(ScrollHResult.maxProperty())));
        canvasCarte.decalageYProperty().bind(ScrollVCarte.valueProperty().multiply(-1).multiply(canvas.widthProperty().divide(ScrollVCarte.maxProperty())));
    }
}
