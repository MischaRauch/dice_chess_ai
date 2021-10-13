package gui;

import com.sun.tools.javac.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Game;
import logic.Move;
import logic.enums.Piece;
import logic.enums.Side;

import javax.swing.*;
import java.io.IOException;

public class MainContainerController {
    private Label displayTurn;
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

    @FXML private AnchorPane modalDialogPane;

    public static AnchorPane modal;

    @FXML private VBox chessVBox;

    private VBox scrollVBox;

    ChessBoard board;


    @FXML
    void initialize() throws IOException{
        quitButton.setOnMouseEntered(event -> quitButton.setStyle("-fx-background-color: #FF0A0A;"));
        quitButton.setOnMouseExited(event -> quitButton.setStyle("-fx-background-color: #bf7832;"));

        modal = modalDialogPane;
        //GridPane board = FXMLLoader.load(getClass().getResource("/fxml/gameboard.fxml"));
        chessVBox.getChildren().add(board = new ChessBoard(this)); //how do I make it non-static?
        chessVBox.setAlignment(Pos.CENTER);

        Parent p =  FXMLLoader.load(getClass().getResource("/fxml/dice.fxml"));

        undoRedoBox = new HBox();
        undoButton = new Button("Undo");
        undoButton.setPrefSize(100, 50);
        undoButton.setOnMouseClicked(e -> {
            Game game = Game.getInstance();
            game.undoState();
            board.loadBoard(game.getCurrentState().toFEN());
        });
        redoButton = new Button("Redo");
        redoButton.setPrefSize(100, 50);
        redoButton.setOnMouseClicked(e ->{
            Game game = Game.getInstance();
            game.redoState();
            board.loadBoard(game.getCurrentState().toFEN());
        });

        undoRedoBox.setAlignment(Pos.CENTER);
        undoRedoBox.setSpacing(10);
        undoRedoBox.getChildren().addAll(undoButton, redoButton);

        scrollVBox = new VBox();

        historyScrollPane = new ScrollPane();
        historyScrollPane.setFitToHeight(true);
        historyScrollPane.setFitToWidth(true);
        historyScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        historyScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        historyScrollPane.setPrefViewportWidth(1000);
        historyScrollPane.setPrefViewportHeight(1000);
        historyScrollPane.setContent(scrollVBox);


        Label scrollpaneTitle = new Label("History of Moves:");
        scrollpaneTitle.setFont(new Font("Cambria", 18));
        scrollpaneTitle.setUnderline(true);
        scrollpaneTitle.setStyle("-fx-font-weight: bold");

        displayTurn = new Label("Turn: " + "WHITE");
        displayTurn.setUnderline(true);
        displayTurn.setFont(new Font("Cambria", 40));
        displayTurn.setStyle("-fx-font-weight: bold");

        rightSideVBox.setSpacing(20);
        rightSideVBox.getChildren().addAll(displayTurn, p, undoRedoBox, scrollpaneTitle, historyScrollPane);

        outFlowPaneW.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, null, null)));
        outFlowPaneW.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.EMPTY)));
        outFlowPaneB.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
        outFlowPaneB.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.EMPTY)));
    }

    public void setInFlowPaneW(ImageView piece){
        piece.setFitWidth(80);
        piece.setFitHeight(80);
        ObservableList list = outFlowPaneW.getChildren();
        list.add(piece);
        System.out.println("ADDED");
    }

    public void setInFlowPaneB(ImageView piece){
        piece.setFitWidth(80);
        piece.setFitHeight(80);
        ObservableList list = outFlowPaneB.getChildren();
        list.add(piece);
        System.out.println("ADDED");
    }

    public void setInScrollPane(Move move){
        Label newL = new Label(move.toString());
        //Label newL = new Label("Hello World");
        newL.setFont(new Font("Arial", 13));
        scrollVBox.getChildren().add(newL);
        scrollVBox.setAlignment(Pos.TOP_CENTER);
    }

    public void setGameForTurn(Game game) {
        displayTurn.setText("Turn: " + game.getTurn());
    }
}
