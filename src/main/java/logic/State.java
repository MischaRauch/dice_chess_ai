package logic;

import logic.board.Board;
import logic.enums.CastlingRights;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import java.util.EnumSet;
import logic.Game;
import static logic.enums.Piece.*;
import static logic.enums.Side.BLACK;
import static logic.enums.Side.WHITE;

public class State {

    public static int gameOver;
    public static int counterForSpaces = 0;
    private boolean applyCastling = false;
    private boolean shortCastlingWhite = true;
    private boolean longCastlingWhite = true;
    private boolean shortCastlingBlack = true;
    private boolean longCastlingBlack = true;
    public Square castling = Square.INVALID;
    public Board board;
    public int diceRoll;
    public Side color;
    public String fen = "";
    public Square enPassant = Square.INVALID;
    private final boolean DEBUG = false;

    EnumSet<CastlingRights> castleRights = EnumSet.allOf(CastlingRights.class);
    EnumSet<Piece> availableWhitePieces = EnumSet.of(WHITE_PAWN, WHITE_KNIGHT, WHITE_BISHOP, WHITE_ROOK, WHITE_QUEEN, WHITE_KING);
    EnumSet<Piece> availableBlackPieces = EnumSet.of(BLACK_PAWN, BLACK_KNIGHT, BLACK_BISHOP, BLACK_ROOK, BLACK_QUEEN, BLACK_KING);

    public State(Board board, int diceRoll, Side color) {
        this.board = board;
        this.diceRoll = diceRoll;
        this.color = color;
    }
    public State(Board board, int diceRoll, Side color, boolean applyCastling, boolean shortCastlingBlack, boolean shortCastlingWhite, boolean longCastlingBlack, boolean longCastlingWhite, Square castling) {
        this.board = board;
        this.diceRoll = diceRoll;
        this.color = color;
        this.applyCastling = applyCastling;
        this.shortCastlingBlack = shortCastlingBlack;
        this.shortCastlingWhite = shortCastlingWhite;
        this.longCastlingBlack = longCastlingBlack;
        this.longCastlingWhite = longCastlingWhite;
        this.castling = castling;
    }

    public static void main(String[] args) {
        String tricky = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R";
        String openingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        // System.out.println("\n" + tricky);
        Game game = new Game(tricky);
        // game.getCurrentState().board.printBoard();
        // System.out.println(game.getCurrentState().toFEN() + "  " + tricky.equals(game.getCurrentState().toFEN()));
    }

    public Board getBoard() {
        return this.board;
    }

    public int getGameOver() {
        return gameOver;
    }
    //Getter for castling
    public boolean isApplyCastling() { return applyCastling; }

    public boolean isShortCastlingWhite() { return shortCastlingWhite; }

    public boolean isLongCastlingWhite() { return longCastlingWhite; }

    public boolean isShortCastlingBlack() { return shortCastlingBlack; }

    public boolean isLongCastlingBlack() { return longCastlingBlack; }
    //Setter for castling
    public void setApplyCastling(boolean applyCastling) { this.applyCastling = applyCastling; }

    public void setShortCastlingWhite(boolean shortCastlingWhite) { this.shortCastlingWhite = shortCastlingWhite; }

    public void setLongCastlingWhite(boolean longCastlingWhite) { this.longCastlingWhite = longCastlingWhite; }

    public void setShortCastlingBlack(boolean shortCastlingBlack) { this.shortCastlingBlack = shortCastlingBlack; }

    public void setLongCastlingBlack(boolean longCastlingBlack) { this.longCastlingBlack = longCastlingBlack; }

    public State applyMove(Move move) {
        //check if last move was castling
        if (Game.castlingPerformed != 0) {
            if (Game.castlingPerformed == 1) {
               // castling = Square.INVALID;
                //this.applyCastling = false;
                this.setShortCastlingWhite(false);
            }
            if (Game.castlingPerformed == 2) {
              //  castling = Square.INVALID;
                //this.applyCastling = false;
                this.setShortCastlingBlack(false);
            }
            if (Game.castlingPerformed == 3) {
               // castling = Square.INVALID;
                //this.applyCastling = false;
                this.setLongCastlingWhite(false);
            }
            if (Game.castlingPerformed == 4) {
               // castling = Square.INVALID;
                //this.applyCastling = false;
                this.setLongCastlingBlack(false);
            }
        }
        //extract castling en passant dice roll

        //check if king got captured
        if (board.getPieceAt(move.getDestination()) == WHITE_KING) {
            gameOver = -1;
        }

        if (board.getPieceAt(move.getDestination()) == BLACK_KING) {
            gameOver = 1;
        }

        int newRoll = Dice.roll();

        Side nextTurn = color == WHITE ? BLACK : WHITE;

        if (DEBUG) {
            System.out.println("PIECE FOR CASTLING " + this.castling);
            System.out.println("Boolean for apply castling: " + this.applyCastling);
            System.out.println("Boolean for castling S B " + this.shortCastlingBlack);
            System.out.println("Boolean for castling S W " + this.shortCastlingWhite);
            System.out.println("Boolean for castling L B " + this.longCastlingBlack);
            System.out.println("Boolean for castling L W " + this.longCastlingWhite);
        }

        //update available pieces sets
        Board newBoard = board.movePiece(move.origin, move.destination);

        if (move.enPassantCapture) {
            newBoard.removePiece(color == WHITE ? move.destination.getSquareBelow() : move.destination.getSquareAbove());
        }
        //check if castling has happend and the rook needs to move
        if (applyCastling) {
            // (1) if (shortCastlingWhite || shortCastlingBlack || longCastlingBlack || longCastlingWhite) {
            // (2) if ((shortCastlingWhite || shortCastlingBlack || longCastlingBlack || longCastlingWhite)) = false {
            // (1) would check till all castle moves are not possible anymore at the beginning of the game, while
            //(2) would check till the end of the game if castling is possible --> I created a new applyCastling boolean
            //to check if castling was done - more efficient over the long run but not optimal
            if (DEBUG) System.out.println("WHY");
            if (this.castling != Square.INVALID) {
                //check which rook has to move based on the setted square
                if (this.castling == Square.f1) {
                    newBoard.setPiece(EMPTY, Square.h1);
                    newBoard.setPiece(WHITE_ROOK, this.castling);
                    longCastlingWhite = false;
                    move.castling = this.castling;
                }
                if (this.castling == Square.d1) {
                    newBoard.setPiece(EMPTY, Square.a1);
                    newBoard.setPiece(WHITE_ROOK, this.castling);
                    shortCastlingWhite = false;
                    move.castling = this.castling;
                }
                if (this.castling == Square.f8) {
                    newBoard.setPiece(EMPTY, Square.h8);
                    newBoard.setPiece(BLACK_ROOK, Square.f8);
                    longCastlingBlack = false;
                    move.castling = this.castling;
                }
                if (this.castling == Square.d8) {
                    newBoard.setPiece(EMPTY, Square.a8);
                    newBoard.setPiece(BLACK_ROOK, this.castling);
                    shortCastlingBlack = false;
                    move.castling = this.castling;
                }
            }
            //applyCastling = false;
        }

        if (move.promotionMove) {
            newBoard.setPiece(move.promotionPiece, move.destination);
        }

        State nextState = new State(newBoard, newRoll, nextTurn, applyCastling, shortCastlingBlack, shortCastlingWhite, longCastlingBlack, longCastlingWhite, castling);

        if (move.enPassantMove) {
            nextState.enPassant = move.enPassant;
        }

        nextState.diceRoll = Dice.roll(nextState, nextTurn);

        // newBoard.printBoard();
        return nextState;
    }

    public String toFEN() {
        String fen = "";
        Piece prev = OFF_BOARD;
        int emptySpaces = 0;

        for (int i = 0; i < board.getBoard().length; i++) {
            Piece p = board.getBoard()[i];
            if (prev == OFF_BOARD && p == OFF_BOARD && i < 116) {
                fen += "/"; //reached end of rank
                i += 6;     //skip forward over off board pieces to next rank
                emptySpaces = 0;   //reset empty spaces
            }

            if (p == EMPTY)
                emptySpaces++;

            if (prev == EMPTY && p != EMPTY)
                fen += emptySpaces + "";   //reached end of empty spaces, print amount

            if (p != EMPTY && p != OFF_BOARD) {
                fen += p.getCharType();     //non-empty piece
                emptySpaces = 0;            //reset empty spaces counter
            }

            prev = p;
        }

        return fen;
    }


}
