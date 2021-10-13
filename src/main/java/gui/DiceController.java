package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.event.ActionEvent;
import logic.Dice;

public class DiceController {

    @FXML
    private VBox image;

    @FXML
    private Button rollButton;

    @FXML
    void rollButtonAction(ActionEvent event) {
        image.getChildren().clear();
        LoadChessImages trial = new LoadChessImages();
        image.getChildren().add(trial.loadImage(trial.whichPiece(Dice.roll(), false)));
    }

}
