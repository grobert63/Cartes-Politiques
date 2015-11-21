package SHPDecoder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Controller {

    @FXML
    Label label;

    @FXML
    Button button;

    void initialize()
    {
    }

    public void buttonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
