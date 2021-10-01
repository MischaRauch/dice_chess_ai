import gui.GridBoard;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DiceChess extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane pane = new BorderPane();

        GridBoard grid = new GridBoard();
        Parent root = grid.create();

        Button quitButton = new Button("QUIT");
        quitButton.setOnMouseClicked(e ->{
            System.exit(0);
        });

        //INSTRUCTIONS
        Button diceChessInstructionsButton = new Button("Recap on Dice Chess");

        //OFF BOARD
        FlowPane leftOffBoard = new FlowPane();
        leftOffBoard.setPadding(new Insets(5,2,5,2));
        //leftOffBoard.setPrefWrapLength(170);
        leftOffBoard.setVgap(4);
        leftOffBoard.setHgap(4);

        FlowPane rightOffBoard = new FlowPane();
        rightOffBoard.setPadding(new Insets(5,100,5,2));
        rightOffBoard.setPrefWrapLength(170);
        rightOffBoard.setVgap(4);
        rightOffBoard.setHgap(4);

        for(int i = 0; i < 16; i++){
            Label offBoardLabel = new Label("OFF");
            offBoardLabel.setPrefSize(60, 60);
            leftOffBoard.getChildren().add(offBoardLabel);
            rightOffBoard.getChildren().add(offBoardLabel);
        }

        pane.setCenter(root);
        pane.setRight(quitButton);
        //pane.setTop(rightOffBoard);
        //pane.setLeft(diceChessInstructionsButton);

        Scene scene = new Scene(pane);

        stage.getIcons().add(new Image("/images/DiceChessIcon.png"));

        stage.setTitle("Dice Chess!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}