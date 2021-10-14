package gui.controllers;

import gui.ChessBoard;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.Game;
import logic.Move;

import java.io.IOException;

public class MainContainerController {
    public static AnchorPane modal;
    ChessBoard board;
    private Label displayTurn;
    private HBox undoRedoBox;
    private Button undoButton;
    private Button redoButton;
    private ScrollPane historyScrollPane;
    @FXML
    private FlowPane outFlowPaneW;
    @FXML
    private FlowPane outFlowPaneB;
    @FXML
    private Button quitButton;
    @FXML
    private Pane boardPane;
    @FXML
    private VBox rightSideVBox;
    @FXML
    private HBox MainHbox;
    @FXML
    private AnchorPane modalDialogPane;
    @FXML
    private VBox chessVBox;

    private VBox scrollVBox;

    @FXML
    void quitEvent(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void initialize() throws IOException {
        quitButton.setOnMouseEntered(event -> quitButton.setStyle("-fx-background-color: #FF0A0A;"));
        quitButton.setOnMouseExited(event -> quitButton.setStyle("-fx-background-color: #bf7832;"));

        modal = modalDialogPane;
        //GridPane board = FXMLLoader.load(getClass().getResource("/fxml/gameboard.fxml"));
        chessVBox.getChildren().add(board = new ChessBoard(this)); //how do I make it non-static?
        chessVBox.setAlignment(Pos.CENTER);

        Parent p = FXMLLoader.load(getClass().getResource("/fxml/dice.fxml"));

        undoRedoBox = new HBox();
        undoButton = new Button("Undo");
        undoButton.setPrefSize(100, 60);
        undoButton.setStyle("-fx-text-fill: #ffffff");
        undoButton.setStyle("-fx-background-color: #bf7832;");
        undoButton.setCursor(Cursor.HAND);
        undoButton.setOnMouseEntered(event -> undoButton.setStyle("-fx-background-color: #bf5500;"));
        undoButton.setOnMouseClicked(e -> {
            Game game = Game.getInstance();
            game.undoState();
            board.loadBoard(game.getCurrentState().toFEN());
        });
        undoButton.setOnMousePressed(event -> undoButton.setStyle("-fx-background-color: #2ecc71"));
        undoButton.setOnMouseReleased(event -> undoButton.setStyle("-fx-background-color: #bf7832;"));
        undoButton.setOnMouseExited(event -> undoButton.setStyle("-fx-background-color: #bf7832;"));

        redoButton = new Button("Redo");
        redoButton.setPrefSize(100, 60);
        redoButton.setStyle("-fx-text-fill: #ffffff");
        redoButton.setStyle("-fx-background-color: #bf7832;");
        redoButton.setCursor(Cursor.HAND);
        redoButton.setOnMouseEntered(event -> redoButton.setStyle("-fx-background-color: #bf5500;"));
        redoButton.setOnMouseClicked(e -> {
            Game game = Game.getInstance();
            game.redoState();
            board.loadBoard(game.getCurrentState().toFEN());
        });
        redoButton.setOnMousePressed(event -> redoButton.setStyle("-fx-background-color: #2ecc71"));
        redoButton.setOnMouseReleased(event -> redoButton.setStyle("-fx-background-color: #bf7832;"));
        redoButton.setOnMouseExited(event -> redoButton.setStyle("-fx-background-color: #bf7832;"));

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
        scrollpaneTitle.setFont(new Font("System", 18));
        scrollpaneTitle.setStyle("-fx-font-weight: bold");
        scrollpaneTitle.setStyle("-fx-text-fill: #ffffff;");

        displayTurn = new Label("Turn: " + "WHITE");
        displayTurn.setFont(new Font("System", 40));
        displayTurn.setStyle("-fx-font-weight: bold");
        displayTurn.setStyle("-fx-text-fill: #000000;");

        rightSideVBox.setSpacing(20);
        rightSideVBox.getChildren().addAll(displayTurn, p, undoRedoBox, scrollpaneTitle, historyScrollPane);

        outFlowPaneW.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, null, null)));
        outFlowPaneW.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.EMPTY)));
        outFlowPaneB.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
        outFlowPaneB.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.EMPTY)));
    }

    public void setInFlowPaneW(ImageView piece) {
        piece.setFitWidth(80);
        piece.setFitHeight(80);
        ObservableList list = outFlowPaneW.getChildren();
        list.add(piece);
        System.out.println("ADDED");
    }

    public void setInFlowPaneB(ImageView piece) {
        piece.setFitWidth(80);
        piece.setFitHeight(80);
        ObservableList list = outFlowPaneB.getChildren();
        list.add(piece);
        System.out.println("ADDED");
    }

    public void setInScrollPane(Move move) {
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
