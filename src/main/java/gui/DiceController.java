package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.event.ActionEvent;
import logic.Dice;
import logic.Game;
import logic.enums.Side;

public class DiceController {

    @FXML
    private VBox image;

    @FXML
    private Button rollButton;

    @FXML
    void rollButtonAction(ActionEvent event) {
        image.getChildren().clear();
        int diceRoll = Game.getInstance().getDiceRoll();
        Side color = Game.getInstance().getTurn();
        image.getChildren().add(ChessIcons.load(diceRoll, color));
    }

}
