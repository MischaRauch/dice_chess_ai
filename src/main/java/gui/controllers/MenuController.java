package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import logic.enums.Side;

import java.io.IOException;

/**
 * Here's an example of a basic JavaFX controller for an FXML file. The @FXML tags indicated this are fields to be injected
 * with the corresponding fx:id or method name from the fxml file.
 * This is a static controller, there are many other and more complex ways to do this
 * The constructor of a controller class gets called before any of the @FXML fields get populated, so use the @FXML initialize
 * method instead. Generally static controllers don't have constructors anyways though.
 */
public class MenuController {

    @FXML
    private Button newGameBtn;

    @FXML
    private Label messageLabel;

    @FXML
    private Button newGameBtnAI;

    @FXML
    private RadioButton whiteRB;

    @FXML
    private RadioButton blackRB;

    @FXML
    void newGamePlayer(ActionEvent event) throws IOException {
        messageLabel.setText("dice chess yay!");
        Stage stage = (Stage) newGameBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainContainer.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMinWidth(1400);
        stage.setMinHeight(930);
        stage.centerOnScreen();

    }

    @FXML
    void newGameAI(ActionEvent event) throws IOException {

        if(whiteRB.isSelected() || blackRB.isSelected()) {
            messageLabel.setText("dice chess yay!");
            Stage stage = (Stage) newGameBtn.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/mainContainer.fxml"));
            Parent root = loader.load();

            MainContainerController controller = loader.getController();
            if(whiteRB.isSelected())
                controller.initData(Side.BLACK); //AI is the opposite of what the player chose
            else
                controller.initData(Side.WHITE);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMinWidth(1400);
            stage.setMinHeight(930);
            stage.centerOnScreen();
        }
    }

    @FXML
    void initialize() throws IOException{
        //this method gets automatically called after the FXMLLoader loads the fxml file into this controller class
        newGameBtn.setOnMouseEntered(event -> newGameBtn.setStyle("-fx-background-color: #27ae60;"));
        newGameBtn.setOnMouseExited(event -> newGameBtn.setStyle("-fx-background-color: #bf7832;"));


        newGameBtnAI.setOnMouseEntered(event -> newGameBtnAI.setStyle("-fx-background-color: #27ae60;"));
        newGameBtnAI.setOnMouseExited(event -> newGameBtnAI.setStyle("-fx-background-color: #bf7832;"));

        ToggleGroup toggle = new ToggleGroup();
        whiteRB.setToggleGroup(toggle);
        blackRB.setToggleGroup(toggle);
    }


}
