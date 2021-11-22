package gui.controllers;

import logic.enums.GameType;
import logic.enums.Piece;
import logic.enums.Side;
import logic.game.*;
import gui.ChessIcons;
import gui.Chessboard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import logic.Move;
import logic.PieceAndTurnDeathTuple;
import logic.player.BasicAIPlayer;
import logic.player.MiniMaxPlayer;
import java.io.IOException;
import java.util.Stack;

public class MainContainerController extends AnchorPane {

    public static boolean inputBlock = false;
    public static Stage stage;
    public static AnchorPane modal;

    private static MainContainerController instance;

    public GameType type;
    private Chessboard board;

    private final Stack<String> guiStringHistoryOfPreviousMoves = new Stack<>();
    private final Stack<String> guiStringHistoryOfRedoMoves = new Stack<>();

    public MainContainerController(GameType type) throws IOException {
        this.type = type;
        instance = this;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainContainer.fxml"));
        loader.setController(this); //this class is the controller for the FXML view that the FXMLLoader is loading
        loader.setRoot(this);       //this class is also the Parent node of the FXML view
        loader.load();
    }

    @FXML
    private Button undoButton;

    @FXML
    private Button redoButton;

    @FXML
    private ImageView diceImage;

    @FXML
    private VBox moveHistory;

    @FXML
    private VBox chessBoardContainer;

    @FXML
    private FlowPane whiteGraveyard;

    @FXML
    private FlowPane blackGraveyard;

    @FXML
    private AnchorPane modalDialog;


    @FXML
    void rollDice(ActionEvent event) {
        if (!inputBlock) {
            int roll = Game.getInstance().getDiceRoll();
            Side color = Game.getInstance().getTurn();
            diceImage.setImage(ChessIcons.load(roll, color).getImage());
        }
    }

    @FXML
    void initialize() throws IOException {
        switch (type) {
            case AI_V_AI -> {
                Game game = new AiAiGame(new BasicAIPlayer(Side.WHITE), new MiniMaxPlayer(Side.BLACK));
            }
            case HUMAN_V_AI -> {
                Game game = new AIGame(new MiniMaxPlayer(Side.BLACK));
            }
            case HUMAN_V_HUMAN -> {
                Game game = new HumanGame();
            }

        }

        board = new Chessboard(type);
        chessBoardContainer.getChildren().add(board);

        modal = modalDialog;
        undoButton.setOnMouseEntered(event -> undoButton.setStyle("-fx-background-color: #bf5500; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        undoButton.setOnMouseExited(event -> undoButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        undoButton.setOnMousePressed(event -> undoButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        undoButton.setOnMouseReleased(event -> undoButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));

        redoButton.setOnMouseEntered(event -> redoButton.setStyle("-fx-background-color: #bf5500; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        redoButton.setOnMouseExited(event -> redoButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        redoButton.setOnMousePressed(event -> redoButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
        redoButton.setOnMouseReleased(event -> redoButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: #ffffff; -fx-background-radius: 5px;"));
    }

    @FXML
    void undoMove(ActionEvent event) {
        if (!inputBlock) {
            Game game = Game.getInstance();
            game.undoState();
            board.loadBoard(game.getCurrentState().toFEN());
            removeLastInScrollPane();
            removeInFlowPanelB();
            removeInFlowPanelW();
        }
    }

    @FXML
    void redoMove(ActionEvent event) {
        if (!inputBlock) {
            Game game = Game.getInstance();
            game.redoState();
            board.loadBoard(game.getCurrentState().toFEN());
            redoInScrollPane();
            redoInFlowPanelB();
            redoInFlowPanelW();
        }
    }

    public void movePieceOut(Piece piece, Side color) {
        ImageView view;
        Game game = Game.getInstance();
        if (color == Side.WHITE) {
            view = piece != Piece.EMPTY ? ChessIcons.load(piece) : new ImageView();
            /// it was setInFlowPaneB, fixed, was this intentional?
            setInFlowPaneW(view);
            // marks piece as dead
            game.getDeadWhitePieces().push(new PieceAndTurnDeathTuple(piece, game.getPreviousStates().size()));
        } else {
            view = piece != Piece.EMPTY ? ChessIcons.load(piece) : new ImageView();
            /// it was setInFlowPaneW, was this intentional?
            setInFlowPaneB(view);
            // marks piece as dead
            game.getDeadBlackPieces().push(new PieceAndTurnDeathTuple(piece, game.getPreviousStates().size()));
        }
    }

    public void setInFlowPaneW(ImageView piece) {
        piece.setFitWidth(60);
        piece.setFitHeight(60);
        whiteGraveyard.getChildren().add(piece);
    }

    public void setInFlowPaneB(ImageView piece) {
        piece.setFitWidth(60);
        piece.setFitHeight(60);
        blackGraveyard.getChildren().add(piece);;
    }

    // adds previously dead piece, now dead again, piece back to flow panel
    public void redoInFlowPanelB() {
        Game game = Game.getInstance();
        if(!game.getRedoDeadBlackPieces().isEmpty()){
            if(game.getRedoDeadBlackPieces().peek().getTurnDeath() == guiStringHistoryOfPreviousMoves.size()) {
                movePieceOut(game.getRedoDeadBlackPieces().peek().getPiece(), Side.BLACK);
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
                movePieceOut(game.getRedoDeadWhitePieces().peek().getPiece(), Side.WHITE);
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
            if (game.getDeadWhitePieces().peek().getTurnDeath() > guiStringHistoryOfPreviousMoves.size() && whiteGraveyard.getChildren().size()!=0) {
                whiteGraveyard.getChildren().remove(whiteGraveyard.getChildren().size() - 1, whiteGraveyard.getChildren().size());
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
            if(game.getDeadBlackPieces().peek().getTurnDeath() > guiStringHistoryOfPreviousMoves.size()  && blackGraveyard.getChildren().size()!=0) {
                blackGraveyard.getChildren().remove(blackGraveyard.getChildren().size()-1,blackGraveyard.getChildren().size());
                PieceAndTurnDeathTuple temp = game.getDeadBlackPieces().peek();
                game.getRedoDeadBlackPieces().push(temp);
                game.getDeadBlackPieces().pop();
            }
        }

    }

    //used in ChessBoard
    //adds move string to previous moves stack
    public void setInScrollPane(Move move) {
        guiStringHistoryOfPreviousMoves.push(move.stylized());
        Label newL = new Label(move.stylized());
        newL.setFont(new Font("Arial", 16));
        moveHistory.getChildren().add(newL);
    }

    //only visual gui string tracking of moves
    //removes the last move string in redo moves stack
    private void redoInScrollPane() {
        if (!guiStringHistoryOfRedoMoves.isEmpty()) {
            String temp = guiStringHistoryOfRedoMoves.peek();
            Label newL = new Label(temp);
            newL.setFont(new Font("Arial", 16));
            moveHistory.getChildren().add(newL);
            //moveHistory.setAlignment(Pos.TOP_CENTER);
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
            moveHistory.getChildren().remove(moveHistory.getChildren().size() - 1, moveHistory.getChildren().size());
        }
    }

    public void setDiceImage(ImageView img) {
        diceImage.setImage(img.getImage());
    }

    public static MainContainerController getInstance() {
        return instance;
    }

}
