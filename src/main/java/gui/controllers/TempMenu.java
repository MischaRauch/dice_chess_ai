package gui.controllers;


import logic.enums.GameType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TempMenu {

    @FXML
    void newAvA(ActionEvent event) {
        loadGameScreen(event, GameType.AI_V_AI);
    }

    @FXML
    void newAvH(ActionEvent event) {
        loadGameScreen(event, GameType.HUMAN_V_AI);
    }

    @FXML
    void newHvA(ActionEvent event) {
        loadGameScreen(event, GameType.HUMAN_V_AI);
    }

    @FXML
    void newHvH(ActionEvent event) {
        loadGameScreen(event, GameType.HUMAN_V_HUMAN);
    }

    private void loadGameScreen(ActionEvent event, GameType type) {

        try {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            MainContainerController.stage = stage;
            Parent root = new MainContainerController(type);
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
