package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ExampleController {

    @FXML
    private VBox container;

    @FXML
    void initialize() throws IOException {
        GridPane root = FXMLLoader.load(getClass().getResource("/fxml/gameboard.fxml"));
        container.getChildren().add(root);

    }

}
