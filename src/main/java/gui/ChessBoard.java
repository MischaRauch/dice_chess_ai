package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import logic.Dice;
import logic.Game;
import logic.LegalMoveGenerator;
import logic.Move;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.enums.Validity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static logic.enums.Side.WHITE;

/**
 * This class is the controller and root of just the game board view. Extra methods can be added to make working with GridPane
 * easier, such as methods to get a Tile at a certain coordinate, or get the Tile associated with a Square enum (e.g. get(Square.e5) would return the Tile at e5, could be useful for Move stuff idk), etc.
 * This class inherits all the methods from GridPane as well btw
 */
public class ChessBoard extends GridPane {

    private final boolean DEBUG = true;
    private final Game game;
    MainContainerController mainContainerController;

    //you can add parameters to the constructor, e.g.: a reference to the greater ApplicationController or whatever,
    //that this class is loaded into, if needed
    public ChessBoard(MainContainerController mainContainerController) throws IOException {
        super(); //honestly idk if this line is even necessary but ive seen it done on the internet
        game = new Game();
        this.mainContainerController = mainContainerController;

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
        if(DEBUG)System.out.println(boardState.length + "len");

        for (int i = 1; i < boardState.length; i++) {
            for (int j = 1; j < boardState.length; j++) {

                // tile gets colored during construction
                Tile tile = new Tile(boardState[i][j], i - 1, j - 1); //0-index the row/col
                //since ChessBoard is a GridPane, we add elements using this.add();

                this.add(tile, j, i);
                tileBoard[i-1][j-1] = tile;

                tile.setOnMouseClicked(event -> {
                    System.out.println(tile.getSquare() + " : " + tile.getPiece());

                    // if there is a Piece in vbox that is no the EMPTY Piece
                    if (tile.getPiece() != Piece.EMPTY) {
                            if (tile.getPiece().getType() == Dice.diceToPiece[game.getDiceRoll() - 1] || !tile.getPiece().isFriendly(game.getTurn()) || tile.getPiece().getType()==Piece.PAWN) {
                                if (Tile.selectedTile == null) {
                                    if (tile.getPiece().isFriendly(game.getTurn())) {
                                        //can only select your own pieces
                                        tile.select();

                                        LegalMoveGenerator gen = new LegalMoveGenerator();
                                        ArrayList<Square> legalMoves = gen.getLegalMoves(game.getCurrentState(),tile.getSquare(),tile.getPiece(),tile.getPiece().getColor());

                                        // on piece click first time, selection
//                              // hard to work with matrix system and loops, doing manually
                                        // TODO add loop system
                                        if(legalMoves.contains(Square.a8)) {tileBoard[0][0].colorGreen();}
                                        if(legalMoves.contains(Square.b8)) {tileBoard[0][1].colorGreen();}
                                        if(legalMoves.contains(Square.c8)) {tileBoard[0][2].colorGreen();}
                                        if(legalMoves.contains(Square.d8)) {tileBoard[0][3].colorGreen();}
                                        if(legalMoves.contains(Square.e8)) {tileBoard[0][4].colorGreen();}
                                        if(legalMoves.contains(Square.f8)) {tileBoard[0][5].colorGreen();}
                                        if(legalMoves.contains(Square.g8)) {tileBoard[0][6].colorGreen();}
                                        if(legalMoves.contains(Square.h8)) {tileBoard[0][7].colorGreen();}

                                        if(legalMoves.contains(Square.a7)) {tileBoard[1][0].colorGreen();}
                                        if(legalMoves.contains(Square.b7)) {tileBoard[1][1].colorGreen();}
                                        if(legalMoves.contains(Square.c7)) {tileBoard[1][2].colorGreen();}
                                        if(legalMoves.contains(Square.d7)) {tileBoard[1][3].colorGreen();}
                                        if(legalMoves.contains(Square.e7)) {tileBoard[1][4].colorGreen();}
                                        if(legalMoves.contains(Square.f7)) {tileBoard[1][5].colorGreen();}
                                        if(legalMoves.contains(Square.g7)) {tileBoard[1][6].colorGreen();}
                                        if(legalMoves.contains(Square.h7)) {tileBoard[1][7].colorGreen();}

                                        if(legalMoves.contains(Square.a6)) {tileBoard[2][0].colorGreen();}
                                        if(legalMoves.contains(Square.b6)) {tileBoard[2][1].colorGreen();}
                                        if(legalMoves.contains(Square.c6)) {tileBoard[2][2].colorGreen();}
                                        if(legalMoves.contains(Square.d6)) {tileBoard[2][3].colorGreen();}
                                        if(legalMoves.contains(Square.e6)) {tileBoard[2][4].colorGreen();}
                                        if(legalMoves.contains(Square.f6)) {tileBoard[2][5].colorGreen();}
                                        if(legalMoves.contains(Square.g6)) {tileBoard[2][6].colorGreen();}
                                        if(legalMoves.contains(Square.h6)) {tileBoard[2][7].colorGreen();}

                                        if(legalMoves.contains(Square.a5)) {tileBoard[3][0].colorGreen();}
                                        if(legalMoves.contains(Square.b5)) {tileBoard[3][1].colorGreen();}
                                        if(legalMoves.contains(Square.c5)) {tileBoard[3][2].colorGreen();}
                                        if(legalMoves.contains(Square.d5)) {tileBoard[3][3].colorGreen();}
                                        if(legalMoves.contains(Square.e5)) {tileBoard[3][4].colorGreen();}
                                        if(legalMoves.contains(Square.f5)) {tileBoard[3][5].colorGreen();}
                                        if(legalMoves.contains(Square.g5)) {tileBoard[3][6].colorGreen();}
                                        if(legalMoves.contains(Square.h5)) {tileBoard[3][7].colorGreen();}

                                        if(legalMoves.contains(Square.a4)) {tileBoard[4][0].colorGreen();}
                                        if(legalMoves.contains(Square.b4)) {tileBoard[4][1].colorGreen();}
                                        if(legalMoves.contains(Square.c4)) {tileBoard[4][2].colorGreen();}
                                        if(legalMoves.contains(Square.d4)) {tileBoard[4][3].colorGreen();}
                                        if(legalMoves.contains(Square.e4)) {tileBoard[4][4].colorGreen();}
                                        if(legalMoves.contains(Square.f4)) {tileBoard[4][5].colorGreen();}
                                        if(legalMoves.contains(Square.g4)) {tileBoard[4][6].colorGreen();}
                                        if(legalMoves.contains(Square.h4)) {tileBoard[4][7].colorGreen();}

                                        if(legalMoves.contains(Square.a3)) {tileBoard[5][0].colorGreen();}
                                        if(legalMoves.contains(Square.b3)) {tileBoard[5][1].colorGreen();}
                                        if(legalMoves.contains(Square.c3)) {tileBoard[5][2].colorGreen();}
                                        if(legalMoves.contains(Square.d3)) {tileBoard[5][3].colorGreen();}
                                        if(legalMoves.contains(Square.e3)) {tileBoard[5][4].colorGreen();}
                                        if(legalMoves.contains(Square.f3)) {tileBoard[5][5].colorGreen();}
                                        if(legalMoves.contains(Square.g3)) {tileBoard[5][6].colorGreen();}
                                        if(legalMoves.contains(Square.h3)) {tileBoard[5][7].colorGreen();}

                                        if(legalMoves.contains(Square.a2)) {tileBoard[6][0].colorGreen();}
                                        if(legalMoves.contains(Square.b2)) {tileBoard[6][1].colorGreen();}
                                        if(legalMoves.contains(Square.c2)) {tileBoard[6][2].colorGreen();}
                                        if(legalMoves.contains(Square.d2)) {tileBoard[6][3].colorGreen();}
                                        if(legalMoves.contains(Square.e2)) {tileBoard[6][4].colorGreen();}
                                        if(legalMoves.contains(Square.f2)) {tileBoard[6][5].colorGreen();}
                                        if(legalMoves.contains(Square.g2)) {tileBoard[6][6].colorGreen();}
                                        if(legalMoves.contains(Square.h2)) {tileBoard[6][7].colorGreen();}

                                        if(legalMoves.contains(Square.a1)) {tileBoard[7][0].colorGreen();}
                                        if(legalMoves.contains(Square.b1)) {tileBoard[7][1].colorGreen();}
                                        if(legalMoves.contains(Square.c1)) {tileBoard[7][2].colorGreen();}
                                        if(legalMoves.contains(Square.d1)) {tileBoard[7][3].colorGreen();}
                                        if(legalMoves.contains(Square.e1)) {tileBoard[7][4].colorGreen();}
                                        if(legalMoves.contains(Square.f1)) {tileBoard[7][5].colorGreen();}
                                        if(legalMoves.contains(Square.g1)) {tileBoard[7][6].colorGreen();}
                                        if(legalMoves.contains(Square.h1)) {tileBoard[7][7].colorGreen();}

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
                tileBoard[i][j].colorDefault();
            }
        }
    }


    // you only move selected tile ever
    private void move(Tile tile) {

        Move move = new Move(Tile.selectedTile.getPiece(), Tile.selectedTile.getSquare(), tile.getSquare(), game.getDiceRoll(), game.getTurn());
        Move applied = game.makeMove(move);

        if (applied.getStatus() == Validity.VALID) {
            //set in scrollPane
            mainContainerController.setInScrollPane(move);
            mainContainerController.setGameForTurn(game);

            if((tile.getPiece() != Piece.EMPTY) && (tile.getPiece().getColor() != Tile.selectedTile.getPiece().getColor())){
                //capture piece so move piece to the flowpane
                movePieceOut(tile.getPiece(), tile.getPiece().getColor());
            }

            tile.setPiece(Tile.selectedTile.getPiece());
            Tile.selectedTile.setPiece(Piece.EMPTY);

            Tile.selectedTile.unselect();
            recolorBoard();

            if (applied.isEnPassantCapture()) {
                //remove captured pawn;
                tileBoard[tile.getRow() + (move.getSide() == WHITE ? 1 : -1)][tile.getCol()].setPiece(Piece.EMPTY);
            }

            if (applied.isPromotionMove()) {
                tile.setPiece(applied.getPromotionPiece());
            }

            //move rook if castling was performed
            if (move.castling != Square.INVALID) {
                //Short castling white
                if(move.castling == Square.f1) {
                    tileBoard[7][7].setPiece(Piece.EMPTY);
                    tileBoard[7][5].setPiece(Piece.WHITE_ROOK);
                }
                //Long castling white
                if(move.castling == Square.d1) {
                    tileBoard[7][0].setPiece(Piece.EMPTY);
                    tileBoard[7][3].setPiece(Piece.WHITE_ROOK);
                }
                //Short castling black
                if(move.castling == Square.f8) {
                    tileBoard[0][7].setPiece(Piece.EMPTY);
                    tileBoard[0][5].setPiece(Piece.BLACK_ROOK);
                }
                //Long castling black
                if(move.castling == Square.d8) {
                    tileBoard[0][0].setPiece(Piece.EMPTY);
                    tileBoard[0][3].setPiece(Piece.BLACK_ROOK);
                }
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

    public void movePieceOut(Piece piece, Side color){
        ImageView view;
        if (color == WHITE){
            view = piece != Piece.EMPTY ? ChessIcons.load(piece) : new ImageView();
            mainContainerController.setInFlowPaneB(view);
        }
        else{
            view = piece != Piece.EMPTY ? ChessIcons.load(piece) : new ImageView();
            mainContainerController.setInFlowPaneW(view);
        }

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

