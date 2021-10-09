package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import logic.Game;
import logic.Move;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.enums.Validity;

import java.io.IOException;
import java.util.Arrays;

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameboard2.fxml"));
        loader.setController(this); //this class is the controller for the FXML view that the FXMLLoader is loading
        loader.setRoot(this);       //this class is also the Parent node of the FXML view
        loader.load();              //this is the method that actually does the loading. It's non-static version of FXMLLoader.load()

        game = new Game();
    }

    //This stuff gets called after the constructor has finished loading the FXML file
    @FXML
    void initialize() {
        String opening = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1 0";   //initial state
        loadBoard(opening);
    }

    //populates the GridPane (which is actually this class) with Tile objects
    //more or less copy-pasted from GameboardController with some slight modifications
    public void loadBoard(String fenD) {
        char[][] boardState = parseFENd(fenD);
        System.out.println(Arrays.deepToString(boardState));
        for (int i = 1; i < boardState.length; i++) {
            for (int j = 1; j < boardState.length; j++) {

                Tile tile = new Tile(boardState[i][j], i-1, j-1); //0-index the row/col
                //since ChessBoard is a GridPane, we add elements using this.add();
                this.add(tile, j, i);

                tile.setOnMouseClicked(event -> {
                    System.out.println(tile.getSquare() + " : " + tile.getPiece());
                    //the event handler can technically also be made in the constructor in the Tile class,
                    //but it might be better to have it here so that you can use other fields/info from this class (e.g. currently selected piece/tile)
                    //that you would not have access to from the Tile class unless everything in here
                    //is static or if each tile has access to an instance of this class

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
            System.out.println("c");

            Tile.selectedTile.unselect();

            System.out.println("Next dice roll: " + game.getDiceRoll());

            //tile.setPiece(Piece.WHITE_ROOK);
            //Move move1 = new Move(Piece.WHITE_ROOK, Square.h1, Square.d4,1,Side.WHITE);
            //castling();
        } else {
            System.out.println("INVALID move");
        }

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
            char[] rank = board[i+1];
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
    public void castling() {
      // Move move = new Move(Tile.selectedTile.getPiece(), Tile.selectedTile.getSquare(), tile.getSquare(), game.getDiceRoll(), game.getTurn());
        Tile tile1 = (Tile) this.getChildren().get(140);
        System.out.println("WHATS THAT: "+tile1.getPiece());

    }
}

