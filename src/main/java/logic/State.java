package logic;

import logic.board.Board;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.game.Game;

import java.util.ArrayList;
import java.util.List;

import static logic.enums.Piece.*;
import static logic.enums.Side.BLACK;
import static logic.enums.Side.WHITE;

public class State {

    //public boolean gameOver = false;
    public Square castling = Square.INVALID;
    public Board board;
    public int diceRoll;
    public Side color;
    public Square enPassant = Square.INVALID;
    private boolean applyCastling = false;
    private boolean shortCastlingWhite = true;
    private boolean longCastlingWhite = true;
    private boolean shortCastlingBlack = true;
    private boolean longCastlingBlack = true;
    private List<PieceAndSquareTuple> pieceAndSquare = new ArrayList<>();
    private int cumulativeTurn;

    // initial
    public State(Board board, int diceRoll, Side color) {
        this.board = board;
        this.diceRoll = diceRoll;
        this.color = color;
        loadPieceAndSquareFromFEN(board.getFEN());
        printPieceAndSquare();
        cumulativeTurn = 0;
    }

    // deep cloning initial state
    public State(State that) {
        cumulativeTurn = 0;
        board = that.getBoard();
        diceRoll = that.getDiceRoll();
        color = that.getColor();
        loadPieceAndSquareFromFEN(that.getBoard().getFEN());
    }

    // for state updation
    public State(Board board, int diceRoll, Side color, boolean applyCastling, boolean shortCastlingBlack, boolean shortCastlingWhite,
                 boolean longCastlingBlack, boolean longCastlingWhite, Square castling, List<PieceAndSquareTuple> pieceAndSquare, int cumulativeTurn) {
        this.board = board;
        this.diceRoll = diceRoll;
        this.color = color;
        this.applyCastling = applyCastling;
        this.shortCastlingBlack = shortCastlingBlack;
        this.shortCastlingWhite = shortCastlingWhite;
        this.longCastlingBlack = longCastlingBlack;
        this.longCastlingWhite = longCastlingWhite;
        this.castling = castling;
        this.pieceAndSquare = pieceAndSquare;
        this.cumulativeTurn = cumulativeTurn;
    }

    public void printPieceAndSquare() {
        System.out.println("State; pieceAndSquare: Size: " + pieceAndSquare.size());
        for (PieceAndSquareTuple t : pieceAndSquare) {
            System.out.print(t.toString() + " | ");
        }
        printPieceCounts(pieceAndSquare);
    }

    private void loadPieceAndSquareFromFEN(String FEN) {
        String[] info = FEN.split("/|\\s"); //either split on "/" or on " " (whitespace)
        for (int i = 0; i < 8; i++) {
            char[] row = info[i].toCharArray();
            int file = 0;
            for (char c : row) {
                if (c == 57) {
                    file += 8;
                } else if (c == 49) {
                    file++; // 49 is same as '1'
                } else if (c == 50) {
                    file += 2;
                } else if (c == 51) {
                    file += 3;
                } else if (c == 52) {
                    file += 4;
                } else if (c == 53) {
                    file += 5;
                } else if (c == 54) {
                    file += 6;
                } else if (c == 55) {
                    file += 7;
                } else if (c == 'p') {
                    pieceAndSquare.add(new PieceAndSquareTuple(BLACK_PAWN, Square.getSquare(7 - i, file)));
                    file++;
                } else if (c == 'n') {
                    pieceAndSquare.add(new PieceAndSquareTuple(BLACK_KNIGHT, Square.getSquare(7 - i, file)));
                    file++;
                } else if (c == 'b') {
                    pieceAndSquare.add(new PieceAndSquareTuple(BLACK_BISHOP, Square.getSquare(7 - i, file)));
                    file++;
                } else if (c == 'r') {
                    pieceAndSquare.add(new PieceAndSquareTuple(BLACK_ROOK, Square.getSquare(7 - i, file)));
                    file++;
                } else if (c == 'q') {
                    pieceAndSquare.add(new PieceAndSquareTuple(BLACK_QUEEN, Square.getSquare(7 - i, file)));
                    file++;
                } else if (c == 'k') {
                    pieceAndSquare.add(new PieceAndSquareTuple(BLACK_KING, Square.getSquare(7 - i, file)));
                    file++;

                } else if (c == 'P') {
                    pieceAndSquare.add(new PieceAndSquareTuple(WHITE_PAWN, Square.getSquare(7 - i, file)));
                    file++;
                } else if (c == 'N') {
                    pieceAndSquare.add(new PieceAndSquareTuple(WHITE_KNIGHT, Square.getSquare(7 - i, file)));
                    file++;
                } else if (c == 'B') {
                    pieceAndSquare.add(new PieceAndSquareTuple(WHITE_BISHOP, Square.getSquare(7 - i, file)));
                    file++;
                } else if (c == 'R') {
                    pieceAndSquare.add(new PieceAndSquareTuple(WHITE_ROOK, Square.getSquare(7 - i, file)));
                    file++;
                } else if (c == 'Q') {
                    pieceAndSquare.add(new PieceAndSquareTuple(WHITE_QUEEN, Square.getSquare(7 - i, file)));
                    file++;
                } else if (c == 'K') {
                    pieceAndSquare.add(new PieceAndSquareTuple(WHITE_KING, Square.getSquare(7 - i, file)));
                    file++;
                }
            }
        }

    }

    public void printPieceCounts(List<PieceAndSquareTuple> pieceAndSquare) {
        int pawn = 0;
        int knight = 0;
        int rook = 0;
        int bishop = 0;
        int king = 0;
        int queen = 0;
        for (PieceAndSquareTuple t : pieceAndSquare) {
            if (t.getPiece().equals(Piece.BLACK_QUEEN) || t.getPiece().equals(Piece.WHITE_QUEEN)) {
                queen++;
            } else if (t.getPiece().equals(Piece.WHITE_BISHOP) || t.getPiece().equals(Piece.BLACK_BISHOP)) {
                bishop++;
            } else if (t.getPiece().equals(Piece.WHITE_KING) || t.getPiece().equals(Piece.BLACK_KING)) {
                king++;
            } else if (t.getPiece().equals(Piece.WHITE_ROOK) || t.getPiece().equals(Piece.BLACK_ROOK)) {
                rook++;
            } else if (t.getPiece().equals(Piece.WHITE_PAWN) || t.getPiece().equals(Piece.BLACK_PAWN)) {
                pawn++;
            } else if (t.getPiece().equals(Piece.WHITE_KNIGHT) || t.getPiece().equals(Piece.BLACK_KNIGHT)) {
                knight++;
            }
        }
        System.out.println("\nCounts: Pawn: " + pawn + " Knight: " + knight + " Bishop: " + bishop + " Rook: " + rook + " Queen: " + queen + " King: " + king + "\n");
    }

    public State applyMove(Move move) {


        //check if last move was castling
        if (Game.getInstance().getCastlingPerformed() != 0) {
            if (Game.getInstance().getCastlingPerformed() == 1) {
                this.setShortCastlingWhite(false);
            }
            if (Game.getInstance().getCastlingPerformed() == 2) {
                this.setShortCastlingBlack(false);
            }
            if (Game.getInstance().getCastlingPerformed() == 3) {
                this.setLongCastlingWhite(false);
            }
            if (Game.getInstance().getCastlingPerformed() == 4) {
                this.setLongCastlingBlack(false);
            }
        }

        Side nextTurn = color == WHITE ? BLACK : WHITE;

        //update available pieces sets
        Board newBoard = board.movePiece(move.origin, move.destination);

        if (move.enPassantCapture) {
            newBoard.removePiece(color == WHITE ? move.destination.getSquareBelow() : move.destination.getSquareAbove());
        }

        //check if castling has happend and the rook needs to move
        if (applyCastling) {
            // (1) if (shortCastlingWhite || shortCastlingBlack || longCastlingBlack || longCastlingWhite) {
            // (2) if ((shortCastlingWhite || shortCastlingBlack || longCastlingBlack || longCastlingWhite)) = false {
            // (1) would check till all castle moves are not possible anymore at the beginning of the logic.game, while
            //(2) would check till the end of the logic.game if castling is possible --> I created a new applyCastling boolean
            //to check if castling was done - more efficient over the long run but not optimal
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
        }

        // update state in case of pawn pr
        if (move.promotionMove) {
            newBoard.setPiece(move.promotionPiece, move.destination);
            updatePieceAndSquareState(new Move(move.promotionPiece, move.getOrigin(), move.getDestination(), move.getDiceRoll(), move.getSide()));
        } else {
            updatePieceAndSquareState(move);
            printPieceAndSquare();
            if (applyCastling && this.castling != Square.INVALID) {
                System.out.println("updated piece and square castling: ");
                if (this.castling == Square.f1)
                    updatePieceAndSquareStateForCastling(Square.h1, Square.f1); // short white
                if (this.castling == Square.d1)
                    updatePieceAndSquareStateForCastling(Square.a1, Square.d1); // long white
                if (this.castling == Square.f8)
                    updatePieceAndSquareStateForCastling(Square.h8, Square.f8); // short black
                if (this.castling == Square.d8)
                    updatePieceAndSquareStateForCastling(Square.a8, Square.d8); // long black
            }
            System.out.println("size: " + pieceAndSquare.size());
        }

        System.out.println("Real cumulative turn: " + cumulativeTurn);
        State nextState = new State(newBoard, -1, nextTurn, applyCastling, shortCastlingBlack, shortCastlingWhite,
                longCastlingBlack, longCastlingWhite, castling, pieceAndSquare, cumulativeTurn + 1);

        if (move.enPassantMove) {
            nextState.enPassant = move.enPassant;
        }

        //get legal dice rolls according to updated new state
        //overwrites the 'newRoll' parameter in the constructor. There must be a better way to do this.
        nextState.diceRoll = Dice.roll(nextState, nextTurn);

        newBoard.printBoard();
        return nextState;
        //}
        //return this;
    }

    private void updatePieceAndSquareStateForCastling(Square rookSquareOld, Square rookSquareNew) {
        PieceAndSquareTuple rookToNewSquare = new PieceAndSquareTuple(WHITE_ROOK, rookSquareNew);
        List<PieceAndSquareTuple> newState = new ArrayList<>();
        // add all tuples except for the old rook that moved
        for (PieceAndSquareTuple t : pieceAndSquare) {
            if (!(t.getPiece() == WHITE_ROOK && t.getSquare() == rookSquareOld)) {
                newState.add(t);
            }
        }
        newState.add(rookToNewSquare);
        pieceAndSquare = newState;
    }

    private void updatePieceAndSquareState(Move move) {
        PieceAndSquareTuple destination = new PieceAndSquareTuple(move.getPiece(), move.getDestination());
        List<PieceAndSquareTuple> newState = new ArrayList<>();
        // skip over tuple that is in origin square or is in destination square
        for (PieceAndSquareTuple t : pieceAndSquare) {
            if (!(t.getPiece() == move.getPiece() && t.getSquare() == move.getOrigin()) && t.getSquare() != move.getDestination()) {
                newState.add(t);
            }
        }
        newState.add(destination);
        pieceAndSquare = newState;
    }

    public String toFEN() {
        String fen = "";
        Piece prev = OFF_BOARD;
        int emptySpaces = 0;

        for (int i = 0; i < board.getBoard().length; i++) {
            Piece p = board.getBoard()[i];
            if (prev == OFF_BOARD && p == OFF_BOARD && i < 116) {
                fen += "/"; //reached end of rank
                i += 6;     //skip forward over off-board pieces to next rank
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

    public Square getCastling() {
        return castling;
    }

    public void setCastling(Square castling) {
        this.castling = castling;
    }

    public int getDiceRoll() {
        return diceRoll;
    }

    public void setDiceRoll(int diceRoll) {
        this.diceRoll = diceRoll;
    }

    public Side getColor() {
        return color;
    }

    public void setColor(Side color) {
        this.color = color;
    }

    public Square getEnPassant() {
        return enPassant;
    }

    public void setEnPassant(Square enPassant) {
        this.enPassant = enPassant;
    }

    public int getKingCount(List<PieceAndSquareTuple> pieceAndSquare) {
        int king = 0;
        for (PieceAndSquareTuple t : pieceAndSquare) {
            if (t.getPiece().equals(Piece.WHITE_KING) || t.getPiece().equals(Piece.BLACK_KING)) {
                king++;
            }
        }
        return king;
    }

    public List<PieceAndSquareTuple> getPieceAndSquare() {
        return pieceAndSquare;
    }

    public void setPieceAndSquare(List<PieceAndSquareTuple> pieceAndSquare) {
        this.pieceAndSquare = pieceAndSquare;
        printPieceAndSquare();
    }

    public int getCumulativeTurn() {
        return cumulativeTurn;
    }

    public void setCumulativeTurn(int cumulativeTurn) {
        this.cumulativeTurn = cumulativeTurn;
    }

    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    //Getter for castling
    public boolean isApplyCastling() {
        return applyCastling;
    }

    //Setter for castling
    public void setApplyCastling(boolean applyCastling) {
        this.applyCastling = applyCastling;
    }

    public boolean isShortCastlingWhite() {
        return shortCastlingWhite;
    }

    public void setShortCastlingWhite(boolean shortCastlingWhite) {
        this.shortCastlingWhite = shortCastlingWhite;
    }

    public boolean isLongCastlingWhite() {
        return longCastlingWhite;
    }

    public void setLongCastlingWhite(boolean longCastlingWhite) {
        this.longCastlingWhite = longCastlingWhite;
    }

    public boolean isShortCastlingBlack() {
        return shortCastlingBlack;
    }

    public void setShortCastlingBlack(boolean shortCastlingBlack) {
        this.shortCastlingBlack = shortCastlingBlack;
    }

    public boolean isLongCastlingBlack() {
        return longCastlingBlack;
    }

    public void setLongCastlingBlack(boolean longCastlingBlack) {
        this.longCastlingBlack = longCastlingBlack;
    }

}
