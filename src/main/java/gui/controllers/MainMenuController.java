package gui.controllers;

import javafx.scene.control.Button;
import logic.enums.GameType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Button HvH;

    @FXML
    private Button HvAI;

    @FXML
    private Button AIvH;

    @FXML
    private Button AIvAI;

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

    @FXML
    void initialize() throws IOException {
        HvH.setOnMouseEntered(event -> HvH.setStyle("-fx-background-color: #bf5500; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        HvH.setOnMouseExited(event -> HvH.setStyle("-fx-background-color: #2980b9; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        HvH.setOnMousePressed(event -> HvH.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        HvH.setOnMouseReleased(event -> HvH.setStyle("-fx-background-color: #2980b9; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));

        HvAI.setOnMouseEntered(event -> HvAI.setStyle("-fx-background-color: #bf5500; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        HvAI.setOnMouseExited(event -> HvAI.setStyle("-fx-background-color: #2980b9; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        HvAI.setOnMousePressed(event -> HvAI.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        HvAI.setOnMouseReleased(event -> HvAI.setStyle("-fx-background-color: #2980b9; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));

        AIvH.setOnMouseEntered(event -> AIvH.setStyle("-fx-background-color: #bf5500; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        AIvH.setOnMouseExited(event -> AIvH.setStyle("-fx-background-color: #2980b9; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        AIvH.setOnMousePressed(event -> AIvH.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        AIvH.setOnMouseReleased(event -> AIvH.setStyle("-fx-background-color: #2980b9; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));

        AIvAI.setOnMouseEntered(event -> AIvAI.setStyle("-fx-background-color: #bf5500; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        AIvAI.setOnMouseExited(event -> AIvAI.setStyle("-fx-background-color: #2980b9; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        AIvAI.setOnMousePressed(event -> AIvAI.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        AIvAI.setOnMouseReleased(event -> AIvAI.setStyle("-fx-background-color: #2980b9; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
    }
}
