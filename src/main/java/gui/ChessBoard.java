package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import logic.Game;
import logic.LegalMoveEvaluator;
import logic.LegalMoveGenerator;
import logic.Move;
import logic.board.Board;
import logic.enums.Piece;
import logic.enums.Square;
import logic.enums.Validity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is the controller and root of just the game board view. Extra methods can be added to make working with GridPane
 * easier, such as methods to get a Tile at a certain coordinate, or get the Tile associated with a Square enum (e.g. get(Square.e5) would return the Tile at e5, could be useful for Move stuff idk), etc.
 * This class inherits all the methods from GridPane as well btw
 */
public class ChessBoard extends GridPane {

    private final Game game;
    private Tile[][] tileMatrix;

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
        tileMatrix = new Tile[8][8];
        char[][] boardState = parseFENd(fenD);
        System.out.println(Arrays.deepToString(boardState));
        for (int i = 1; i < boardState.length; i++) {
            for (int j = 1; j < boardState.length; j++) {

                // tile gets colored during construction
                Tile tile = new Tile(boardState[i][j], i - 1, j - 1); //0-index the row/col
                //since ChessBoard is a GridPane, we add elements using this.add();

                this.add(tile, j, i);
                tileMatrix[j-1][i-1]=tile;

                tile.setOnMouseClicked(event -> {
                    System.out.println(tile.getSquare() + " : " + tile.getPiece());
                    //the event handler can technically also be made in the constructor in the Tile class,
                    //but it might be better to have it here so that you can use other fields/info from this class (e.g. currently selected piece/tile)
                    //that you would not have access to from the Tile class unless everything in here
                    //is static or if each tile has access to an instance of this class

                    // if there is a Piece in vbox that is no the EMPTY Piece
                    if (tile.getPiece() != Piece.EMPTY) {
                        if (Tile.selectedTile == null) {
                            if (tile.getPiece().isFriendly(game.getTurn())) {
                                //can only select your own pieces
                                tile.select();
                                //implement color tiles green

                                tileMatrix[4][4].colorGreen();

                                LegalMoveGenerator gen = new LegalMoveGenerator();
                                ArrayList<Square> legalMoves = gen.getLegalMoves(game.getCurrentState(),tile.getSquare(),tile.getPiece(),tile.getPiece().getColor());
                                // add all squares
                                for (int k = 0; k < 8; k++) {
                                    for (int l = 0; l < 8; l++) {
                                        //legalMoves.add(Square.getSquare(k,l));
                                        if(legalMoves.contains(Square.getSquare(k,l))) {
                                            tileMatrix[k][l].colorGreen();
                                        }
                                    }
                                }

                                System.out.println(legalMoves);
                                }
                        } else {
                            if (tile == Tile.selectedTile) {
                                //suicide not allowed
                                tile.unselect();
                                tileMatrix[4][4].colorDefault();

                                LegalMoveGenerator gen = new LegalMoveGenerator();
                                ArrayList<Square> legalMoves = gen.getLegalMoves(game.getCurrentState(),tile.getSquare(),tile.getPiece(),tile.getPiece().getColor());
                                for (int k = 0; k < 8; k++) {
                                    for (int l = 0; l < 8; l++) {
                                        //legalMoves.add(Square.getSquare(k,l));
                                        if(legalMoves.contains(Square.getSquare(k,l))) {
                                            tileMatrix[k][l].colorDefault();
                                        }
                                    }
                                }

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
    // used to return tile (that has HBox funcionality)
    private Node getNodeFromGridPane(int col, int row) {
        for (Node node : this.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public Tile getTileAtSquare(Square square) {
        return null;
    }

    // you only move selected tile ever
    private void move(Tile tile) {

        Move move = new Move(Tile.selectedTile.getPiece(), Tile.selectedTile.getSquare(), tile.getSquare(), game.getDiceRoll(), game.getTurn());
        Move appliedMove = game.makeMove(move); //updates Validity in Tile to VALID if move legal

        //todo use array of generated squares to color tiles green


        if (appliedMove.getStatus() == Validity.VALID) { // if legal move evaluator said VALIDITY Enum to VALID in Piece
            tile.setPiece(Tile.selectedTile.getPiece());

            Tile.selectedTile.setPiece(Piece.EMPTY); // sets Piece Enum to empty

            Tile.selectedTile.unselect(); // paints board back to the same

            if (appliedMove.isEnPassantCapture()) {
                //TODO remove captured pawn
            }

            System.out.println("Next dice roll: " + game.getDiceRoll());

            if (game.getCurrentState().getGameOver() != 0) {
                showEndGame(game.getCurrentState().getGameOver());
            }

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

