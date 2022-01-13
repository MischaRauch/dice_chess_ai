package gui.controllers;

import gui.ChessIcons;
import gui.Chessboard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import logic.Move;
import logic.PieceAndTurnDeathTuple;
import logic.enums.GameType;
import logic.enums.Piece;
import logic.enums.Side;
import logic.game.Game;

import java.io.IOException;
import java.util.Stack;

import static logic.enums.Side.WHITE;

public class MainContainerController extends AnchorPane {

    public static boolean inputBlock = false;
    public static Stage stage;
    public static AnchorPane modal;

    private static MainContainerController instance;

    public GameType type;
    private Chessboard board;
    private String openingFEN;

   // private final Stack<String> guiStringHistoryOfPreviousMoves = new Stack<>();
   // private final Stack<String> guiStringHistoryOfRedoMoves = new Stack<>();

    public MainContainerController(GameType type, String openingFEN) throws IOException {
        this.type = type;
        instance = this;
        this.openingFEN = openingFEN;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainContainer.fxml"));
        loader.setController(this); //this class is the controller for the FXML view that the FXMLLoader is loading
        loader.setRoot(this);       //this class is also the Parent node of the FXML view
        loader.load();
    }

    public MainContainerController(Game game) throws IOException {
        //this.type = type;
        instance = this;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainContainer.fxml"));
        loader.setController(this); //this class is the controller for the FXML view that the FXMLLoader is loading
        loader.setRoot(this);       //this class is also the Parent node of the FXML view
        loader.load();
    }


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
    private Label turnIndicator;

    @FXML
    void initialize() throws IOException {
        board = new Chessboard(type, openingFEN);
        chessBoardContainer.getChildren().add(board);

        modal = modalDialog;
        updateTurn(WHITE);

        diceImage.setImage(ChessIcons.load(Game.getInstance().getDiceRoll(), Game.getInstance().getTurn()).getImage());
    }


    public void movePieceOut(Piece piece, Side color) {
        ImageView view;
        Game game = Game.getInstance();
        if (color == WHITE) {
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
        blackGraveyard.getChildren().add(piece);
    }


    //used in ChessBoard
    //adds move string to previous moves stack
    public void setInScrollPane(Move move) {
        Label newL = new Label(move.stylized());
        newL.setFont(new Font("Arial", 16));
        ImageView pieceIcon = ChessIcons.load(move.getPiece());
        pieceIcon.setFitWidth(20);
        pieceIcon.setFitHeight(20);
        newL.setGraphic(pieceIcon);
        moveHistory.getChildren().add(newL);
    }


    public void setDiceImage(ImageView img) {
        diceImage.setImage(img.getImage());
    }

    public void updateTurn(Side color) {
        if (color == WHITE)
            turnIndicator.setText("White's");
        else
            turnIndicator.setText("Black's");
    }

    public static MainContainerController getInstance() {
        return instance;
    }
    public  void setDiceImage(Image image){
        diceImage.setImage(image);
    }
}