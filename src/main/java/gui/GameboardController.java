package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import logic.Dice;

import java.util.HashMap;
import java.util.Map;

public class GameboardController {

    @FXML
    private GridPane guiBoard;

    @FXML
    private Button diceRollButton;

    @FXML
    private Label diceRoll;

    @FXML
    void roll(ActionEvent event) {
        diceRoll.setText(Dice.roll() + "");
    }


    @FXML
    void initialize() {
        //create all pieces
        //set event handlers
        //can access gridpane cells using row and column indices
        //indices start at 1 since row 0 and column 0 are used to display rank and file
        //white cells: row + col % 2 == 0
        //black cells: row + col % 2 == 1
        //(e.g.: in case of conditionally changing piece texture depending on background)


    }

}
