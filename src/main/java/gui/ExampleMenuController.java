package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Here's an example of a basic JavaFX controller for an FXML file. The @FXML tags indicated this are fields to be injected
 * with the corresponding fx:id or method name from the fxml file.
 * This is a static controller, there are many other and more complex ways to do this
 * The constructor of a controller class gets called before any of the @FXML fields get populated, so use the @FXML initialize
 * method instead. Generally static controllers don't have constructors anyways though.
 */
public class ExampleMenuController {

    @FXML
    private Button newGameBtn;

    @FXML
    private Label messageLabel;

    @FXML
    void newGame(ActionEvent event) {
        messageLabel.setText("dice chess yay!");
    }

    @FXML
    void initialize() {
        //this method gets automagically called after the FXMLLoader loads the fxml file into this controller class
        newGameBtn.setOnMouseEntered(event -> newGameBtn.setStyle("-fx-background-color: #27ae60;"));
        newGameBtn.setOnMouseExited(event -> newGameBtn.setStyle("-fx-background-color: #2ecc71;"));

    }

}
