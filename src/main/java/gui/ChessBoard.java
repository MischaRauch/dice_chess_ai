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

    private final boolean DEBUG = true;
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
        if(DEBUG)System.out.println(boardState.length + "len");

        for (int i = 1; i < boardState.length; i++) {
            for (int j = 1; j < boardState.length; j++) {

                // tile gets colored during construction
                Tile tile = new Tile(boardState[i][j], i - 1, j - 1); //0-index the row/col
                //since ChessBoard is a GridPane, we add elements using this.add();

                this.add(tile, j, i);
                //TODO fix this shit
                tileMatrix[i-1][j-1]=tile;

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

                                LegalMoveGenerator gen = new LegalMoveGenerator();
                                ArrayList<Square> legalMoves = gen.getLegalMoves(game.getCurrentState(),tile.getSquare(),tile.getPiece(),tile.getPiece().getColor());

                                // on piece click first time, selection
//                              // hard to work with matrix system and loops, doing manually
                                // TODO add loop system
                                if(legalMoves.contains(Square.a8)) {tileMatrix[0][0].colorGreen();}
                                if(legalMoves.contains(Square.b8)) {tileMatrix[0][1].colorGreen();}
                                if(legalMoves.contains(Square.c8)) {tileMatrix[0][2].colorGreen();}
                                if(legalMoves.contains(Square.d8)) {tileMatrix[0][3].colorGreen();}
                                if(legalMoves.contains(Square.e8)) {tileMatrix[0][4].colorGreen();}
                                if(legalMoves.contains(Square.f8)) {tileMatrix[0][5].colorGreen();}
                                if(legalMoves.contains(Square.g8)) {tileMatrix[0][6].colorGreen();}
                                if(legalMoves.contains(Square.h8)) {tileMatrix[0][7].colorGreen();}

                                if(legalMoves.contains(Square.a7)) {tileMatrix[1][0].colorGreen();}
                                if(legalMoves.contains(Square.b7)) {tileMatrix[1][1].colorGreen();}
                                if(legalMoves.contains(Square.c7)) {tileMatrix[1][2].colorGreen();}
                                if(legalMoves.contains(Square.d7)) {tileMatrix[1][3].colorGreen();}
                                if(legalMoves.contains(Square.e7)) {tileMatrix[1][4].colorGreen();}
                                if(legalMoves.contains(Square.f7)) {tileMatrix[1][5].colorGreen();}
                                if(legalMoves.contains(Square.g7)) {tileMatrix[1][6].colorGreen();}
                                if(legalMoves.contains(Square.h7)) {tileMatrix[1][7].colorGreen();}

                                if(legalMoves.contains(Square.a6)) {tileMatrix[2][0].colorGreen();}
                                if(legalMoves.contains(Square.b6)) {tileMatrix[2][1].colorGreen();}
                                if(legalMoves.contains(Square.c6)) {tileMatrix[2][2].colorGreen();}
                                if(legalMoves.contains(Square.d6)) {tileMatrix[2][3].colorGreen();}
                                if(legalMoves.contains(Square.e6)) {tileMatrix[2][4].colorGreen();}
                                if(legalMoves.contains(Square.f6)) {tileMatrix[2][5].colorGreen();}
                                if(legalMoves.contains(Square.g6)) {tileMatrix[2][6].colorGreen();}
                                if(legalMoves.contains(Square.h6)) {tileMatrix[2][7].colorGreen();}

                                if(legalMoves.contains(Square.a5)) {tileMatrix[3][0].colorGreen();}
                                if(legalMoves.contains(Square.b5)) {tileMatrix[3][1].colorGreen();}
                                if(legalMoves.contains(Square.c5)) {tileMatrix[3][2].colorGreen();}
                                if(legalMoves.contains(Square.d5)) {tileMatrix[3][3].colorGreen();}
                                if(legalMoves.contains(Square.e5)) {tileMatrix[3][4].colorGreen();}
                                if(legalMoves.contains(Square.f5)) {tileMatrix[3][5].colorGreen();}
                                if(legalMoves.contains(Square.g5)) {tileMatrix[3][6].colorGreen();}
                                if(legalMoves.contains(Square.h5)) {tileMatrix[3][7].colorGreen();}

                                if(legalMoves.contains(Square.a4)) {tileMatrix[4][0].colorGreen();}
                                if(legalMoves.contains(Square.b4)) {tileMatrix[4][1].colorGreen();}
                                if(legalMoves.contains(Square.c4)) {tileMatrix[4][2].colorGreen();}
                                if(legalMoves.contains(Square.d4)) {tileMatrix[4][3].colorGreen();}
                                if(legalMoves.contains(Square.e4)) {tileMatrix[4][4].colorGreen();}
                                if(legalMoves.contains(Square.f4)) {tileMatrix[4][5].colorGreen();}
                                if(legalMoves.contains(Square.g4)) {tileMatrix[4][6].colorGreen();}
                                if(legalMoves.contains(Square.h4)) {tileMatrix[4][7].colorGreen();}

                                if(legalMoves.contains(Square.a3)) {tileMatrix[5][0].colorGreen();}
                                if(legalMoves.contains(Square.b3)) {tileMatrix[5][1].colorGreen();}
                                if(legalMoves.contains(Square.c3)) {tileMatrix[5][2].colorGreen();}
                                if(legalMoves.contains(Square.d3)) {tileMatrix[5][3].colorGreen();}
                                if(legalMoves.contains(Square.e3)) {tileMatrix[5][4].colorGreen();}
                                if(legalMoves.contains(Square.f3)) {tileMatrix[5][5].colorGreen();}
                                if(legalMoves.contains(Square.g3)) {tileMatrix[5][6].colorGreen();}
                                if(legalMoves.contains(Square.h3)) {tileMatrix[5][7].colorGreen();}

                                if(legalMoves.contains(Square.a2)) {tileMatrix[6][0].colorGreen();}
                                if(legalMoves.contains(Square.b2)) {tileMatrix[6][1].colorGreen();}
                                if(legalMoves.contains(Square.c2)) {tileMatrix[6][2].colorGreen();}
                                if(legalMoves.contains(Square.d2)) {tileMatrix[6][3].colorGreen();}
                                if(legalMoves.contains(Square.e2)) {tileMatrix[6][4].colorGreen();}
                                if(legalMoves.contains(Square.f2)) {tileMatrix[6][5].colorGreen();}
                                if(legalMoves.contains(Square.g2)) {tileMatrix[6][6].colorGreen();}
                                if(legalMoves.contains(Square.h2)) {tileMatrix[6][7].colorGreen();}

                                if(legalMoves.contains(Square.a1)) {tileMatrix[7][0].colorGreen();}
                                if(legalMoves.contains(Square.b1)) {tileMatrix[7][1].colorGreen();}
                                if(legalMoves.contains(Square.c1)) {tileMatrix[7][2].colorGreen();}
                                if(legalMoves.contains(Square.d1)) {tileMatrix[7][3].colorGreen();}
                                if(legalMoves.contains(Square.e1)) {tileMatrix[7][4].colorGreen();}
                                if(legalMoves.contains(Square.f1)) {tileMatrix[7][5].colorGreen();}
                                if(legalMoves.contains(Square.g1)) {tileMatrix[7][6].colorGreen();}
                                if(legalMoves.contains(Square.h1)) {tileMatrix[7][7].colorGreen();}

                                System.out.println("legal moves: " + legalMoves); //correct,which means legal move gen works
                                }
                        } else {
                            if (tile == Tile.selectedTile) {
                                //suicide not allowed
                                tile.unselect();
                                recolorBoard();
                            } else {
                                //capture
                                if(DEBUG)System.out.println("Capture");
                                recolorBoard();
                                move(tile);

                            }
                        }
                    } else {
                        if (Tile.selectedTile != null) {
                            if(DEBUG)System.out.println("piece not selected");
                            recolorBoard();
                            move(tile);
                        }
                    }

                    //process move, check validity, update gui board, etc
                });

            }
        }
    }

    private void recolorBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tileMatrix[i][j].colorDefault();
            }
        }
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
            recolorBoard();

            if (appliedMove.isEnPassantCapture()) {
                //TODO remove captured pawn
            }

            System.out.println("Next dice roll: " + game.getDiceRoll());

            if (game.getCurrentState().getGameOver() != 0) {
                showEndGame(game.getCurrentState().getGameOver());
            }

        } else {
            // VERY IMPORTANT TO UNSELECT: FIXED BUG
            Tile.selectedTile.unselect();
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

