package gui;

import gui.controllers.GameOverScreen;
import gui.controllers.MainContainerController;
import gui.controllers.Menu;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import logic.Config;
import logic.Dice;
import logic.LegalMoveGenerator;
import logic.Move;
import logic.enums.*;
import logic.game.AIGame;
import logic.game.AiAiGame;
import logic.game.Game;
import logic.game.HumanGame;

import java.io.IOException;
import java.util.List;

import static logic.enums.Side.WHITE;


/**
 * This class is the controller and root of just the logic.game logic.board view. Extra methods can be added to make working with GridPane
 * easier, such as methods to get a Tile at a certain coordinate, or get the Tile associated with a Square enum (e.g. get(Square.e5) would return the Tile at e5, could be useful for Move stuff idk), etc.
 * This class inherits all the methods from GridPane as well btw
 */
public class Chessboard extends GridPane {

    public static Chessboard chessboard;
    private final GameType gameType;
    private final Game game;
    private final Tile[][] tileBoard = new Tile[8][8];
    private String openingFEN;

    //you can add parameters to the constructor, e.g.: a reference to the greater ApplicationController or whatever,
    //that this class is loaded into, if needed
    public Chessboard(GameType type, String openingFEN) throws IOException {
        game = Game.getInstance();
        this.gameType = type;
        chessboard = this;
        this.openingFEN = openingFEN;
        setStyle("-fx-background-color: #ffffff");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameboard.fxml"));
        loader.setController(this); //this class is the controller for the FXML view that the FXMLLoader is loading
        loader.setRoot(this);       //this class is also the Parent node of the FXML view
        loader.load();              //this is the method that actually does the loading. It's non-static version of FXMLLoader.load()
    }

    //This stuff gets called after the constructor has finished loading the FXML file
    @FXML
    void initialize() {
        loadBoard(openingFEN);
    }

    //populates the GridPane (which is actually this class) with Tile objects
    //more or less copy-pasted from GameboardController with some slight modifications
    public void loadBoard(String fenD) {
        if (gameType == GameType.AI_V_AI) {
            //SimulationHandler sH = new SimulationHandler();
            //sH.start();
            if (!Menu.getSingleGame())
                Platform.exit();
        }
        char[][] boardState = parseFENd(fenD);
        for (int i = 1; i < boardState.length; i++) {
            for (int j = 1; j < boardState.length; j++) {
                // tile gets colored during construction
                Tile tile = new Tile(boardState[i][j], i - 1, j - 1); //0-index the row/col
                //since ChessBoard is a GridPane, we add elements using this.add();

                this.add(tile, j, i);
                tileBoard[i - 1][j - 1] = tile;

                if (gameType != GameType.AI_V_AI) {
                    tile.setOnMouseClicked(new MovePieceHandler(tile));
                }
            }
        }

        if (gameType == GameType.AI_V_AI && Menu.getSingleGame()) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    ((AiAiGame) game).start();
                    return null;
                }
            };
            new Thread(task).start();

        }

    }

    private void recolorBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tileBoard[i][j].colorDefault();
            }
        }
    }

    public void updateGUI(Move move) {
        if (move.getStatus() == Validity.VALID) {

            game.setNumTurns(game.getNumTurns() + 1);

            MainContainerController.getInstance().setInScrollPane(move);
            //mainContainerController.setGameForTurn(logic.game);

            Tile origin = getTileAt(8 - move.getOrigin().getRank(), move.getOrigin().getFile());
            Tile destination = getTileAt(8 - move.getDestination().getRank(), move.getDestination().getFile());

            if ((destination.getPiece() != Piece.EMPTY) && (destination.getPiece().getColor() != origin.getPiece().getColor())) {// && !move.isPromotionMove()
                //capture piece so move piece to the flowpanel
                //System.out.println("tile not empty and tile color not selected color");
                MainContainerController.getInstance().movePieceOut(destination.getPiece(), destination.getPiece().getColor());
            }

            destination.setPiece(origin.getPiece());
            origin.setPiece(Piece.EMPTY);
            recolorBoard();

            if (move.isEnPassantCapture()) {
                //remove captured pawn;
                tileBoard[destination.getRow() + (move.getSide() == WHITE ? 1 : -1)][destination.getCol()].setPiece(Piece.EMPTY);
            }

            if (move.isPromotionMove()) {
                destination.setPiece(move.getPromotionPiece());
                // System.out.println("is promotion move");
            }

            //move rook if castling was performed
            if (move.castlingRookDestination != Square.INVALID) {
                //Short castling white
                if (move.castlingRookDestination == Square.f1) {
                    tileBoard[7][7].setPiece(Piece.EMPTY);
                    tileBoard[7][5].setPiece(Piece.WHITE_ROOK);
                }
                //Long castling white
                if (move.castlingRookDestination == Square.d1) {
                    tileBoard[7][0].setPiece(Piece.EMPTY);
                    tileBoard[7][3].setPiece(Piece.WHITE_ROOK);
                }
                //Short castling black
                if (move.castlingRookDestination == Square.f8) {
                    tileBoard[0][7].setPiece(Piece.EMPTY);
                    tileBoard[0][5].setPiece(Piece.BLACK_ROOK);
                }
                //Long castling black
                if (move.castlingRookDestination == Square.d8) {
                    tileBoard[0][0].setPiece(Piece.EMPTY);
                    tileBoard[0][3].setPiece(Piece.BLACK_ROOK);
                }
            }

//            if (game.getCurrentState().getGameOver() != 0) {
//                //showEndGame(logic.game.getCurrentState().getGameOver());
//                //Stage stage = (Stage) getScene().getWindow();
//                MainContainerController.stage.setScene(new Scene(new GameOverScreen(game.getCurrentState().getGameOver() == 1 ? WHITE : BLACK)));
//            }

            if (game.isGameOver()) {
                //showEndGame(logic.game.getCurrentState().getGameOver());
                //Stage stage = (Stage) getScene().getWindow();
                Side winner = game.getWinner();



                MainContainerController.stage.setScene(new Scene(new GameOverScreen(game.getWinner())));

                // if gameover true then the winner has also been set by the checkGameOver( method in Game, so we can reset state here
               if(gameType!=GameType.AI_V_AI) {
                   game.resetCurrentStateToFirstState();
                   game.setGameOver(false);
               }

            }

            }

//            if (game.getCurrentState().getGameOver() != 0) {
//
//                //showEndGame(logic.game.getCurrentState().getGameOver());
//                //Stage stage = (Stage) getScene().getWindow();
//                Side winner = game.getCurrentState().getGameOver() == 1 ? WHITE : BLACK;
//                //Writing CSV file
//                if(gameType == GameType.AI_V_AI) {
//                    AiAiGame aiAiGame = (AiAiGame) game;
//                    handle = new csvHandler(aiAiGame.getAIPlayerWhite().getNameAi(), aiAiGame.getAIPlayerBlack().getNameAi(), winner.name(), game.getNumTurns());
//                    handle.aiVsAiCsvWrite();
//                }
//                else if(gameType == GameType.HUMAN_V_AI){
//                    AIGame aiGame = (AIGame) game;
//                   handle = new csvHandler(gameType.name(), aiGame.getAiPlayerAiGame().getNameAi(), aiGame.getAiPlayerSide().toString(), winner.name(), game.getNumTurns());
//                    handle.addToCsv();
//                }
//                else {
//                    handle = new csvHandler(gameType.name(), "null", "null", winner.name(), game.getNumTurns());
//                    handle.addToCsv();
//                }
//
//                MainContainerController.stage.setScene(new Scene(new GameOverScreen(winner)));
//            }
        }

    // you only move selected tile ever
    private void move(Tile tile) throws CloneNotSupportedException {

        switch (gameType) {
            case HUMAN_V_HUMAN -> {
                HumanGame humanGame = (HumanGame) game;
                Move move = new Move(Tile.selectedTile.getPiece(), Tile.selectedTile.getSquare(), tile.getSquare(), game.getDiceRoll(), game.getTurn());
                move = humanGame.makeHumanMove(move);
                updateGUI(move);
                Tile.selectedTile.unselect();
            }

            case HUMAN_V_AI -> {
                //first apply human's move
                AIGame aiGame = (AIGame) game;
                Move move = new Move(Tile.selectedTile.getPiece(), Tile.selectedTile.getSquare(), tile.getSquare(), game.getDiceRoll(), game.getTurn());
                updateGUI(aiGame.makeHumanMove(move));
                Tile.selectedTile.unselect();

                //then update the GUI with the AI's move
                Move aiMove = aiGame.makeAIMove();
                MainContainerController.getInstance().setDiceImage(ChessIcons.load(aiMove.getDiceRoll(), aiMove.getSide()));

                updateGUI(aiMove);
            }
        }

    }

    public Tile getTileAt(int row, int col) {
        return tileBoard[row][col];
    }

    // copy-pasted from GameboardController and removed some of the unnecessary lines like the two dice rolls
    // however more unnecessary lines can be removed like the activeColor, castling etc; However idk.
    public char[][] parseFENd(String fenDiceBoard) {
        //chess logic.board has starts with index 1 so to keep things simple leave index 0 empty
        char[][] board = new char[9][9];
        String[] info = fenDiceBoard.split("/|\\s"); //either split on "/" or on " " (whitespace)

        for (int i = 0; i < 8; i++) {
            char[] rankSequence = info[i].toCharArray();
            char[] rank = board[i + 1];
            int index = 1;
            for (char c : rankSequence)
                if (Character.isDigit(c))
                    index += Character.getNumericValue(c);
                else if (Character.isAlphabetic(c))
                    rank[index++] = c;
        }

        return board;
    }

    public void showEndGame(int winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End of the Game");
        alert.setHeaderText("Group 04 hopes you enjoyed the logic.game!");
        if (winner == 1)
            alert.setContentText("Good job WHITE you won!");
        else
            alert.setContentText("Good job BLACK you won!");

        alert.showAndWait();
    }

    class MovePieceHandler implements EventHandler<MouseEvent> {

        Tile tile;
        private final LegalMoveGenerator generator = new LegalMoveGenerator();

        public MovePieceHandler(Tile tile) {
            this.tile = tile;
        }

        @Override
        public void handle(MouseEvent event) {
            // if there is a Piece in vbox that is no the EMPTY Piece
            if (tile.getPiece() != Piece.EMPTY) {
                if (tile.getPiece().getType() == Dice.diceToPiece[game.getDiceRoll() - 1] ||
                        !tile.getPiece().isFriendly(game.getTurn()) ||
                        tile.getPiece().promotable(tile.getSquare())) {
                    if (Tile.selectedTile == null) {
                        if (tile.getPiece().isFriendly(game.getTurn())) {
                            //can only select your own pieces
                            tile.select();

                            //color legal moves green
                            List<Square> legalMoves = LegalMoveGenerator.getMoves(game.getCurrentState(), tile.getSquare(), tile.getPiece());
                            for (Square s : legalMoves)
                                tileBoard[8 - s.getRank()][s.getFile()].colorGreen();
                        }
                    } else {
                        if (tile == Tile.selectedTile) {
                            //suicide not allowed
                            tile.unselect();
                            recolorBoard();
                        } else {
                            //capture
                            recolorBoard();
                            try {
                                move(tile);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else {
                if (Tile.selectedTile != null) {
                    recolorBoard();
                    try {
                        move(tile);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }

            MainContainerController.getInstance().setDiceImage(ChessIcons.load(Game.getInstance().getDiceRoll(), Game.getInstance().getTurn()).getImage());

        }
    }
}



