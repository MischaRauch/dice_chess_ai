package gui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.event.ActionEvent;
import logic.Dice;
import logic.Game;
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
    void initialize() throws IOException{
        //container.setBackground(new Background(new BackgroundFill(Color.TURQUOISE, null, null)));
        //container.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL)));
        //imageVBox.setAlignment(Pos.CENTER);
        //container.setAlignment(Pos.CENTER);
    }

}
