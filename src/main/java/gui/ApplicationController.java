package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ApplicationController {

    @FXML
    private HBox boardContainer;

    @FXML
    void initialize() throws IOException {
        boardContainer.getChildren().add(new ChessBoard());
    }
}
