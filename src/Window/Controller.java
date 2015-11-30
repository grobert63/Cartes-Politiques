package Window;

import GUI.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

public class Controller {

    @FXML
    Pane PaneAffichage;

    @FXML
    ScrollBar ScrollHResult;

    @FXML
    ScrollBar ScrollVResult;

    @FXML
    Slider SliderResult;

    @FXML
    Label label;

    @FXML
    Button button;

    @FXML
    void initialize()
    {
        SliderResult.setMin(1);
        SliderResult.setMax(2);
        SliderResult.setBlockIncrement(0.01);
        try {
            GUI.Main.chargement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HexCanvas canvas = new HexCanvas(1000, 700,GUI.Main.grid);
        PaneAffichage.getChildren().add(canvas);
        canvas.widthProperty().bind(
                PaneAffichage.widthProperty());
        canvas.heightProperty().bind(
                PaneAffichage.heightProperty());
        canvas.zoomProperty().bind(SliderResult.valueProperty());
        canvas.decalageXProperty().bind(ScrollHResult.valueProperty().multiply(-1).multiply(canvas.widthProperty().divide(ScrollHResult.maxProperty())));
        canvas.decalageYProperty().bind(ScrollVResult.valueProperty().multiply(-1).multiply(canvas.widthProperty().divide(ScrollHResult.maxProperty())));

    }
}
