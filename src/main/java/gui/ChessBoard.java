package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import logic.Game;
import logic.Move;
import logic.enums.Piece;
import logic.enums.Validity;

import java.io.IOException;
import java.util.Arrays;

import static logic.enums.Side.WHITE;

/**
 * This class is the controller and root of just the game board view. Extra methods can be added to make working with GridPane
 * easier, such as methods to get a Tile at a certain coordinate, or get the Tile associated with a Square enum (e.g. get(Square.e5) would return the Tile at e5, could be useful for Move stuff idk), etc.
 * This class inherits all the methods from GridPane as well btw
 */
public class ChessBoard extends GridPane {

    private final Game game;

    //you can add parameters to the constructor, e.g.: a reference to the greater ApplicationController or whatever,
    //that this class is loaded into, if needed
    public ChessBoard() throws IOException {
        super(); //honestly idk if this line is even necessary but ive seen it done on the internet
        game = new Game();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameboard2.fxml"));
        loader.setController(this); //this class is the controller for the FXML view that the FXMLLoader is loading
        loader.setRoot(this);       //this class is also the Parent node of the FXML view
        loader.load();              //this is the method that actually does the loading. It's non-static version of FXMLLoader.load()
    }

    //This stuff gets called after the constructor has finished loading the FXML file
    @FXML
    void initialize() {
        String opening = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1 0";   //initial state
        loadBoard(opening);
    }

    private final Tile[][] tileBoard = new Tile[8][8];

    //populates the GridPane (which is actually this class) with Tile objects
    //more or less copy-pasted from GameboardController with some slight modifications
    public void loadBoard(String fenD) {
        char[][] boardState = parseFENd(fenD);
        System.out.println(Arrays.deepToString(boardState));
        for (int i = 1; i < boardState.length; i++) {
            for (int j = 1; j < boardState.length; j++) {

                Tile tile = new Tile(boardState[i][j], i - 1, j - 1); //0-index the row/col
                //since ChessBoard is a GridPane, we add elements using this.add();
                this.add(tile, j, i);
                tileBoard[i-1][j-1] = tile;

                tile.setOnMouseClicked(event -> {
                    System.out.println(tile.getSquare() + " : " + tile.getPiece());

                    if (tile.getPiece() != Piece.EMPTY) {
                        if (Tile.selectedTile == null) {
                            if (tile.getPiece().isFriendly(game.getTurn())) {
                                //can only select your own pieces
                                tile.select();
                            }
                        } else {
                            if (tile == Tile.selectedTile) {
                                //suicide not allowed
                                tile.unselect();
                            } else {
                                //capture
                                move(tile);
                            }
                        }
                    } else {
                        if (Tile.selectedTile != null) {
                            move(tile);
                        }
                    }

                    //process move, check validity, update gui board, etc
                });

            }
        }
    }

    private void move(Tile tile) {
        Move move = new Move(Tile.selectedTile.getPiece(), Tile.selectedTile.getSquare(), tile.getSquare(), game.getDiceRoll(), game.getTurn());
        Move applied = game.makeMove(move);

        if (applied.getStatus() == Validity.VALID) {
            tile.setPiece(Tile.selectedTile.getPiece());
            Tile.selectedTile.setPiece(Piece.EMPTY);

            Tile.selectedTile.unselect();

            if (applied.isEnPassantCapture()) {
                //remove captured pawn;
                tileBoard[tile.getRow() + (move.getSide() == WHITE ? 1 : -1)][tile.getCol()].setPiece(Piece.EMPTY);
            }

            System.out.println("Next dice roll: " + game.getDiceRoll());

            if (game.getCurrentState().getGameOver() != 0) {
                showEndGame(game.getCurrentState().getGameOver());
            }


        } else {
            System.out.println("INVALID move");
        }

    }

    public Tile getTileAt(int row, int col) {
        return tileBoard[row][col];
    }

    //copy-pasted from GameboardController and removed some of the unnecessary lines like the two dice rolls
    //however more unnecessary lines can be removed like the activeColor, castling etc; However idk.
    public char[][] parseFENd(String fenDiceBoard) {
        //chess board has starts with index 1 so to keep things simple leave index 0 empty
        char[][] board = new char[9][9];
        String[] info = fenDiceBoard.split("/|\\s"); //either split on "/" or on " " (whitespace)

        String activeColor = info[8];
        String castling = info[9];
        String enPassant = info[10];
        String halfmoveClock = info[11];
        String fullmoveNumber = info[12];

        for (int i = 0; i < 8; i++) {
            char[] rankSequence = info[i].toCharArray();
            char[] rank = board[i + 1];
            int index = 1;
            for (char c : rankSequence) {
                if (Character.isDigit(c)) {
                    index += Character.getNumericValue(c);
                } else if (Character.isAlphabetic(c)) {
                    rank[index++] = c;
                }
            }
        }

        return board;
    }

    public void showEndGame(int winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End of the Game");
        alert.setHeaderText("Group 04 hopes you enjoed the game!");
        if (winner == 1)
            alert.setContentText("Good job WHITE you won!");
        else
            alert.setContentText("Good job BLACK you won!");

        alert.showAndWait();
    }
}

