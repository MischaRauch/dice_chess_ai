package gui;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import logic.Game;

import javax.swing.*;
import java.io.IOException;

public class MainContainerController {

    private HBox undoRedoBox;
    private Button undoButton;
    private Button redoButton;
    private ScrollPane historyScrollPane;

    @FXML private FlowPane outFlowPaneW;

    @FXML private FlowPane outFlowPaneB;

    @FXML private Button quitButton;
    @FXML
    void quitEvent(ActionEvent event){
        System.exit(0);
    }

    @FXML private Pane boardPane;

    @FXML private VBox rightSideVBox;

    @FXML private HBox MainHbox;

    private Game game;


    @FXML
    void initialize() throws IOException{

        //GridPane board = FXMLLoader.load(getClass().getResource("/fxml/gameboard.fxml"));
        MainHbox.getChildren().add(new ChessBoard()); //how do I make it non-static?

        Parent p =  FXMLLoader.load(getClass().getResource("/fxml/dice.fxml"));

        undoRedoBox = new HBox();
        undoButton = new Button("Undo");
        undoButton.setPrefSize(100, 50);
        undoButton.setOnMouseClicked(e ->{
            game = game.getInstance();
            game.getCurrentState().stateToFen();
            game.undoState();
        });
        redoButton = new Button("Redo");
        redoButton.setPrefSize(100, 50);
        redoButton.setOnMouseClicked(e ->{

        });

        undoRedoBox.setAlignment(Pos.CENTER);
        undoRedoBox.setSpacing(10);
        undoRedoBox.getChildren().addAll(undoButton, redoButton);

        historyScrollPane = new ScrollPane();
        historyScrollPane.setFitToHeight(true);
        historyScrollPane.setFitToWidth(true);
        historyScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        historyScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        // historyScrollPane.setContent(//List of moves);

        Label diceTitle = new Label("Dice:");
        Label scrollpaneTitle = new Label("History of Moves:");
        rightSideVBox.setSpacing(20);
        rightSideVBox.getChildren().addAll(diceTitle, p, undoRedoBox, scrollpaneTitle, historyScrollPane);

        outFlowPaneW.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, null, null)));
        outFlowPaneW.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.EMPTY)));
        outFlowPaneB.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
        outFlowPaneB.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.EMPTY)));
    }



}
