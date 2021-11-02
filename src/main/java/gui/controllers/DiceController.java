package gui.controllers;

import gui.ChessIcons;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import logic.game.Game;
import logic.enums.Side;

import java.io.IOException;

public class DiceController {

    @FXML
    private VBox container;
    @FXML
    private VBox imageVBox;

    @FXML
    private Button rollButton;

    @FXML
    void rollButtonAction(ActionEvent event) {
        imageVBox.getChildren().clear();
        int diceRoll = Game.getInstance().getDiceRoll();
        Side color = Game.getInstance().getTurn();
        imageVBox.getChildren().add(ChessIcons.load(diceRoll, color));
    }

    @FXML
    void initialize() throws IOException {
        rollButton.setOnMouseEntered(event -> rollButton.setStyle("-fx-background-color: #27ae60;"));
        rollButton.setOnMouseExited(event -> rollButton.setStyle("-fx-background-color: #bf7832;"));
    }

}
