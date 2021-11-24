package logic;

import gui.controllers.MainContainerController;
import logic.board.Board;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.game.Game;

import static logic.enums.Piece.BLACK_KING;
import static logic.enums.Piece.WHITE_KING;
import static logic.enums.Piece.*;
import static logic.enums.Side.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class State {

    //public static int gameOver;
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
        // TODO optional: add updatePieceAndSquareFromFen so we can load different states
        initializePieceAndSquare();
        this.cumulativeTurn = 0;
    }

    // for state updation
    public State(Board board, int diceRoll, Side color, boolean applyCastling, boolean shortCastlingBlack, boolean shortCastlingWhite,
                 boolean longCastlingBlack, boolean longCastlingWhite, Square castling, List<PieceAndSquareTuple> pieceAndSquare,int cumulativeTurn) {
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

    public State(State toClone) {
        this(toClone.getBoard(),toClone.getDiceRoll(),toClone.getColor(),toClone.isApplyCastling(),toClone.isShortCastlingBlack(),toClone.isShortCastlingWhite(),
                toClone.isLongCastlingBlack(),toClone.isLongCastlingWhite(),toClone.getCastling(),toClone.getPieceAndSquare(),toClone.getCumulativeTurn());
    }

    public void printPieceAndSquare() {
        System.out.println("State; pieceAndSquare: Size: " + pieceAndSquare.size());
        for (PieceAndSquareTuple t : pieceAndSquare) {
            System.out.print(t.toString() + " | ");
        }
        printPieceCounts(pieceAndSquare);
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

//    public int getGameOver() {
//        return gameOver;
//    }
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

    public State applyMove(Move move) {

        //check if last move was castling
        if (Game.castlingPerformed != 0) {
            if (Game.castlingPerformed == 1) {
                this.setShortCastlingWhite(false);
            }
            if (Game.castlingPerformed == 2) {
                this.setShortCastlingBlack(false);
            }
            if (Game.castlingPerformed == 3) {
                this.setLongCastlingWhite(false);
            }
            if (Game.castlingPerformed == 4) {
                this.setLongCastlingBlack(false);
            }
        }
        //extract castling en passant dice roll

        //check if king got captured
//        if (board.getPieceAt(move.getDestination()) == WHITE_KING) {
//            gameOver = -1;
//        }
//
//        if (board.getPieceAt(move.getDestination()) == BLACK_KING) {
//            gameOver = 1;
//        }

        //necessary because the State constructor demands a dice roll. However, this value get pretty much immediately
        //after the next State creation. So really this is unnecessary here I think
        int newRoll = Dice.roll();
        System.out.println("State; applyMove; Dice.roll(): " + newRoll);

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
            //applyCastling = false;
        }


        // update state
        cumulativeTurn++;
        if (move.promotionMove) {
            newBoard.setPiece(move.promotionPiece, move.destination);
            updatePieceAndSquareState(new Move(move.promotionPiece,move.getOrigin(),move.getDestination(),move.getDiceRoll(),move.getSide()));
        } else {
            updatePieceAndSquareState(move);
        }

        // in piece and square the last pieces are the pieces that moved always :)
        // after calling updatePieceAndSquareState if king white (king moved) disable castling
        if(pieceAndSquare.get(pieceAndSquare.size()-1).getPiece()==Piece.WHITE_KING) {
            System.out.println("CASTLING DISABLED WHITE");
            setLongCastlingWhite(false);
            setShortCastlingWhite(false);
        } else if (pieceAndSquare.get(pieceAndSquare.size()-1).getPiece()==Piece.BLACK_KING) {
            System.out.println("CASTLING DISABLED BLACK");
            setLongCastlingBlack(false);
            setShortCastlingBlack(false);
        }


        // update piece and square for move


        System.out.println("Real cumulative turn: " + cumulativeTurn);
        State nextState = new State(newBoard, newRoll, nextTurn, applyCastling, shortCastlingBlack, shortCastlingWhite,
                longCastlingBlack, longCastlingWhite, castling, pieceAndSquare, cumulativeTurn);

        if (move.enPassantMove) {
            nextState.enPassant = move.enPassant;
        }

        //get legal dice rolls according to updated new state
        //overwrites the 'newRoll' parameter in the constructor. There must be a better way to do this.
        nextState.diceRoll = Dice.roll(nextState, nextTurn);

        newBoard.printBoard();
        return nextState;
    }

    private void updatePieceAndSquareState(Move move){
        List<PieceAndSquareTuple> list = new CopyOnWriteArrayList<PieceAndSquareTuple>();
        ListIterator litr = pieceAndSquare.listIterator();
        // loop thorugh pieceAndSquareState
        while(litr.hasNext()) {
            PieceAndSquareTuple t = (PieceAndSquareTuple) litr.next();
            if (t.getSquare() == move.getOrigin()) {
//                if(DEBUG)System.out.println("skipped: " + t.getPiece().toString() + " " + t.getSquare().toString());
            } else {
                list.add((PieceAndSquareTuple)t);
//                if(DEBUG)System.out.println("added: " + t.getPiece().toString() + " " + t.getSquare().toString());
            }
        }
        PieceAndSquareTuple tupleForLeavingSquare = new PieceAndSquareTuple(move.getPiece(), move.getDestination());
        list.add(tupleForLeavingSquare);
        List<PieceAndSquareTuple> list2 = new CopyOnWriteArrayList<PieceAndSquareTuple>();
        // shuffles around pieces in array list and also in memory
        ListIterator litr2 = list.listIterator();
        while(litr2.hasNext()) {
            PieceAndSquareTuple t = (PieceAndSquareTuple) litr2.next();
            if (tupleForLeavingSquare.getSquare()==t.getSquare() &&  litr2.nextIndex()==list.size()-1) {
                litr2.next();
//                if(DEBUG)System.out.println("skipped:2 " + t.getPiece().toString() +" " + t.getSquare().toString());
            } else if (tupleForLeavingSquare.getSquare()!=t.getSquare() && litr2.nextIndex()!=list.size()-1){
                list2.add(t);
//                if(DEBUG)System.out.println("added:3 " + t.getPiece().toString() + " " + t.getSquare().toString());
            } else if (litr2.nextIndex()==list.size()-1){
                list2.add(t);
//                if(DEBUG)System.out.println("added:4" + t.getPiece().toString() + " " + t.getSquare().toString());
            }
        }
        list2.add(tupleForLeavingSquare);
//        if(DEBUG)System.out.println("added:5" + tupleForLeavingSquare.getPiece().toString() + " " + tupleForLeavingSquare.getSquare().toString());
        setPieceAndSquare(list2);
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

    public int getDiceRoll() {
        return diceRoll;
    }

    public Side getColor() {
        return color;
    }

    public Square getEnPassant() {
        return enPassant;
    }

//    public static void setGameOver(int gameOver) {
//        State.gameOver = gameOver;
//    }

    public void setCastling(Square castling) {
        this.castling = castling;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setDiceRoll(int diceRoll) {
        this.diceRoll = diceRoll;
    }

    public void setColor(Side color) {
        this.color = color;
    }

    public void setEnPassant(Square enPassant) {
        this.enPassant = enPassant;
    }

    private void initializePieceAndSquare() {
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_ROOK, Square.a8));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_KNIGHT, Square.b8));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_BISHOP, Square.c8));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_QUEEN, Square.d8));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_KING, Square.e8));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_BISHOP, Square.f8));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_KNIGHT, Square.g8));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_ROOK, Square.h8));

        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_PAWN, Square.a7));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_PAWN, Square.b7));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_PAWN, Square.c7));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_PAWN, Square.d7));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_PAWN, Square.e7));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_PAWN, Square.f7));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_PAWN, Square.g7));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.BLACK_PAWN, Square.h7));

        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_PAWN, Square.a2));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_PAWN, Square.b2));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_PAWN, Square.c2));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_PAWN, Square.d2));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_PAWN, Square.e2));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_PAWN, Square.f2));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_PAWN, Square.g2));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_PAWN, Square.h2));

        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_ROOK, Square.a1));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_KNIGHT, Square.b1));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_BISHOP, Square.c1));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_QUEEN, Square.d1));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_KING, Square.e1));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_BISHOP, Square.f1));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_KNIGHT, Square.g1));
        pieceAndSquare.add(new PieceAndSquareTuple(Piece.WHITE_ROOK, Square.h1));
    }

    private int getKingCount(List<PieceAndSquareTuple> pieceAndSquare) {
        int king = 0;
        for (PieceAndSquareTuple t : pieceAndSquare) {
            if (t.getPiece().equals(Piece.WHITE_KING) || t.getPiece().equals(Piece.BLACK_KING)) {
                king++;
            }
        }
        return king;
    }

}
