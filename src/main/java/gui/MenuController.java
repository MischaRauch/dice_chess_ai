package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Here's an example of a basic JavaFX controller for an FXML file. The @FXML tags indicated this are fields to be injected
 * with the corresponding fx:id or method name from the fxml file.
 * This is a static controller, there are many other and more complex ways to do this
 * The constructor of a controller class gets called before any of the @FXML fields get populated, so use the @FXML initialize
 * method instead. Generally static controllers don't have constructors anyways though.
 */
public class MenuController {

//    @FXML
//    ImageView imageview0;
//    Image myImage = new Image(getClass().getResourceAsStream("Group 4 Project 2.1.jpg"));
//    public void displayImage() {
//        imageview0.setImage(myImage);
//    }

    @FXML
    private Button newGameBtn;

    @FXML
    private Label messageLabel;

    @FXML
    void newGame(ActionEvent event) throws IOException {
        messageLabel.setText("dice chess yay!");
        Stage stage = (Stage) newGameBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/gameboard.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    void initialize() {
        //this method gets automagically called after the FXMLLoader loads the fxml file into this controller class
        newGameBtn.setOnMouseEntered(event -> newGameBtn.setStyle("-fx-background-color: #27ae60;"));
        newGameBtn.setOnMouseExited(event -> newGameBtn.setStyle("-fx-background-color: #2ecc71;"));
    }



}
