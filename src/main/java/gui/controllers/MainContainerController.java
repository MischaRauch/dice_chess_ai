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
import logic.PieceAndTurnDeathTuple;
import logic.enums.Piece;
import logic.enums.Side;
import java.io.IOException;
import java.util.Stack;

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

    private Stack<String> guiStringHistoryOfPreviousMoves = new Stack<>();

    private Stack<String> guiStringHistoryOfRedoMoves = new Stack<>();

    private final boolean DEBUG = false;

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
            removeLastInScrollPane();
            if (DEBUG) {
                System.out.println("UNDO: ");
                System.out.println("outFlowPaneW size W: " + outFlowPaneW.getChildren().size());
                System.out.println("outFlowPaneB size B: " + outFlowPaneB.getChildren().size());
                System.out.println("dead white pieces tuple stack size: " + game.getDeadWhitePieces().size());
                System.out.println("dead black pieces tuple stack size: " + game.getDeadBlackPieces().size());
                System.out.println("dead white pieces tuple stack size: " + game.getDeadWhitePieces().size());
                System.out.println("dead black pieces tuple stack size: " + game.getDeadBlackPieces().size());
            }
            removeInFlowPanelB();
            removeInFlowPanelW();
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
            redoInScrollPane();
            if(DEBUG) {
                System.out.println("REDO: ");
                System.out.println("outFlowPaneW size W: " + outFlowPaneW.getChildren().size());
                System.out.println("outFlowPaneB size B: " + outFlowPaneB.getChildren().size());
                System.out.println("dead white pieces tuple stack size: " + game.getDeadWhitePieces().size());
                System.out.println("dead black pieces tuple stack size: " + game.getDeadBlackPieces().size());
                System.out.println("redo dead black pieces tuple stack size: " + game.getRedoDeadBlackPieces().size());
                System.out.println("redo dead white pieces tuple stack size: " + game.getRedoDeadWhitePieces().size());
            }
            redoInFlowPanelB();
            redoInFlowPanelW();
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
    }

    public void setInFlowPaneB(ImageView piece) {
        piece.setFitWidth(80);
        piece.setFitHeight(80);
        ObservableList list = outFlowPaneB.getChildren();
        list.add(piece);
    }

    // adds previously dead piece, now dead again, piece back to flow panel
    public void redoInFlowPanelB() {
        Game game = Game.getInstance();
        if(!game.getRedoDeadBlackPieces().isEmpty()){
            if(game.getRedoDeadBlackPieces().peek().getTurnDeath() == guiStringHistoryOfPreviousMoves.size()) {
                board.movePieceOut((Piece) game.getRedoDeadBlackPieces().peek().getPiece(), Side.BLACK);
                PieceAndTurnDeathTuple temp = game.getRedoDeadBlackPieces().peek();
                game.getDeadBlackPieces().push(temp);
                game.getRedoDeadBlackPieces().pop();
            }
        }
    }

    // adds previously dead piece, now dead again, piece back to flow panel
    public void redoInFlowPanelW() {
        Game game = Game.getInstance();
        if(!game.getRedoDeadWhitePieces().isEmpty()){
            if(game.getRedoDeadWhitePieces().peek().getTurnDeath() == guiStringHistoryOfPreviousMoves.size()) {
                //move the piece out that was dead
                board.movePieceOut((Piece) game.getRedoDeadWhitePieces().peek().getPiece(), Side.WHITE);
                PieceAndTurnDeathTuple temp = game.getRedoDeadWhitePieces().peek();
                // add the piece that is dead again into the dead pieces, now it's dead again
                game.getDeadWhitePieces().push(temp);
                // remove the piece that is dead again from the redo stack
                game.getRedoDeadWhitePieces().pop();
            }
        }
    }

    // removes now alive piece from flow panel
    public void removeInFlowPanelW() {
        // previous state stack size is number of turns
        Game game = Game.getInstance();
        if (!game.getDeadWhitePieces().isEmpty()) {
            // if the last piece that died, died on a turn that has more than current turn
            if (game.getDeadWhitePieces().peek().getTurnDeath() > guiStringHistoryOfPreviousMoves.size() && outFlowPaneW.getChildren().size()!=0) {
                outFlowPaneW.getChildren().remove(outFlowPaneW.getChildren().size() - 1, outFlowPaneW.getChildren().size());
                PieceAndTurnDeathTuple temp = game.getDeadWhitePieces().peek();
                game.getRedoDeadWhitePieces().push(temp);
                game.getDeadWhitePieces().pop();
            }
        }
    }

    // removes now alive piece from flow panel
    public void removeInFlowPanelB() {
        // previous state stack size is number of turns
        Game game = Game.getInstance();
        if (!game.getDeadBlackPieces().isEmpty()) {
            // if the last piece that died, died on a turn that has more than current turn
            if(game.getDeadBlackPieces().peek().getTurnDeath() > guiStringHistoryOfPreviousMoves.size()  && outFlowPaneB.getChildren().size()!=0) {
                outFlowPaneB.getChildren().remove(outFlowPaneB.getChildren().size()-1,outFlowPaneB.getChildren().size());
                PieceAndTurnDeathTuple temp = game.getDeadBlackPieces().peek();
                game.getRedoDeadBlackPieces().push(temp);
                game.getDeadBlackPieces().pop();
            }
        }

    }

    //used in ChessBoard
    //adds move string to previous moves stack
    public void setInScrollPane(Move move) {
        guiStringHistoryOfPreviousMoves.push(move.toString());
        Label newL = new Label(move.toString());
        newL.setFont(new Font("Arial", 13));
        scrollVBox.getChildren().add(newL);
        scrollVBox.setAlignment(Pos.TOP_CENTER);
        //System.out.println("scrollVBox size: " + scrollVBox.getChildren().size());
    }

    //only visual gui string tracking of moves
    //removes the last move string in redo moves stack
    private void redoInScrollPane() {
        if (!guiStringHistoryOfRedoMoves.isEmpty()) {
            String temp = guiStringHistoryOfRedoMoves.peek();
            Label newL = new Label(temp);
            newL.setFont(new Font("Arial", 13));
            scrollVBox.getChildren().add(newL);
            scrollVBox.setAlignment(Pos.TOP_CENTER);
            guiStringHistoryOfPreviousMoves.push(temp);
            guiStringHistoryOfRedoMoves.pop();
        }
    }

    //only visual gui string tracking of moves
    //removes the last move string in previous moves stack
    private void removeLastInScrollPane() {
        if(!guiStringHistoryOfPreviousMoves.isEmpty()) {
            String current = guiStringHistoryOfPreviousMoves.peek();
            guiStringHistoryOfRedoMoves.push(current);
            guiStringHistoryOfPreviousMoves.pop();
            scrollVBox.getChildren().remove(scrollVBox.getChildren().size() - 1, scrollVBox.getChildren().size());
        }
    }

    public void setGameForTurn(Game game) {
        displayTurn.setText("Turn: " + game.getTurn());
    }
}
