package logic.expectiminimax;

import logic.LegalMoveGenerator;
import logic.PieceAndSquareTuple;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// generates all possible states of the board for n turns ahead
public class BoardStateGenerator {

    // move piece and return array of all possible states for all possible moves a given piece can make taking into account its origin square
    public List<List<PieceAndSquareTuple>> getStateFromLegalMoves(List<PieceAndSquareTuple> nodePieceAndSquare, List<Square> legalMoves, Piece piece, Square origin) {

        List<List<PieceAndSquareTuple>> possibleStates = new ArrayList<>();
        List<PieceAndSquareTuple> nodePieceAndSquareCopy = nodePieceAndSquare.stream().collect(Collectors.toList());
        List<PieceAndSquareTuple> nodePieceAndSquareCopy2 = nodePieceAndSquare.stream().collect(Collectors.toList());

        // loop through state
        for (PieceAndSquareTuple t : nodePieceAndSquareCopy) {
            // you know you are at origin
            if (t.getSquare() == origin && t.getPiece() == piece) {
                // loop through legal moves, update piece and square
                for (int i = 0; i < legalMoves.size(); i++) {

                    // remove previous location
                    for (PieceAndSquareTuple t2 : nodePieceAndSquareCopy) {
                        if (t2.getPiece() == piece && t2.getSquare() == origin) {
                            nodePieceAndSquareCopy2.remove(t2);
                        }
                    }

                    // add new location
                    nodePieceAndSquareCopy2.add(new PieceAndSquareTuple(piece, legalMoves.get(i)));

                    // remove pieces at possible new location
                    for (PieceAndSquareTuple t2 : nodePieceAndSquareCopy) {
                        // never get legal move that goes on same square as friendly piece, assume always different piece (eg. [PIECE]_BLACK if AI is [PIECE]_WHITE)
                        if (t2.getPiece() != piece && t2.getSquare() == legalMoves.get(i)) {
                            nodePieceAndSquareCopy2.remove(t2);
                        }
                    }

                    // add updated pieceAndSquare (state) for (Squares) legalmoves.get(i) to list
                    possibleStates.add(nodePieceAndSquareCopy2);

                    // reset
                    nodePieceAndSquareCopy2 = nodePieceAndSquareCopy.stream().collect(Collectors.toList());
                }
            }
        }
        return possibleStates;
    }

    // list of piece states for a given dice roll and side color, used in minimax
    public List<List<PieceAndSquareTuple>> getPossibleBoardStates(List<PieceAndSquareTuple> nodePieceAndSquare, Side color, int diceRoll, State state) {
        List<PieceAndSquareTuple> nodePieceAndSquareCopy = nodePieceAndSquare.stream().collect(Collectors.toList());
        List<PieceAndSquareTuple> nodePieceAndSquareCopy2 = nodePieceAndSquare.stream().collect(Collectors.toList());

        List<List<PieceAndSquareTuple>> possibleStates = new ArrayList<>();
        //printPieceAndSquare(nodePieceAndSquareCopy);

        for (PieceAndSquareTuple t : nodePieceAndSquareCopy) {
            // clone for casting
            Piece p = (Piece) t.getPiece();
            Square s = (Square) t.getSquare(); //origin
            // get all piece types with their dice numbers
            Piece coloredPiece = Piece.getPieceFromDice(diceRoll, color);
            if (p == coloredPiece) {
                List<Square> legalMoves = LegalMoveGenerator.getLegalMoves(state, s, p, color);
                //printLegalMoves(legalMoves);

                List<List<PieceAndSquareTuple>> states = getStateFromLegalMoves(nodePieceAndSquareCopy2, legalMoves, p, s);

                for (int j = 0; j < states.size(); j++) {
                    possibleStates.add(states.get(j));
                    //printPieceAndSquare(states.get(j));
                }
            }
        }
        return possibleStates;
    }

    // gets a list of all the possible board weights for specific piece for all the possible board states List<PieceAndSquareTuple> nodePieceAndSquare type (i.e. WHITE_PAWN)
    public List<Integer> getPossibleBoardStatesWeightsOfSpecificPiece(List<PieceAndSquareTuple> nodePieceAndSquare, Side color, int diceRoll, State state) {
        List<Integer> possibleBoardStatesWeights = new ArrayList<Integer>();

        List<List<PieceAndSquareTuple>> possibleBoardStates = getPossibleBoardStates(nodePieceAndSquare, color, diceRoll, state);

        for (List<PieceAndSquareTuple> boardState : possibleBoardStates) {
            int newBoardPieceStateWeights = BoardStateEvaluator.getBoardEvaluationNumber(boardState, color);
            //System.out.println("newBoardPieceStateWeights: " + newBoardPieceStateWeights);
            possibleBoardStatesWeights.add(newBoardPieceStateWeights);
        }
        //printPossibleBoardStatesWeights(possibleBoardStatesWeights);
        return possibleBoardStatesWeights;
    }

    public void printPossibleBoardStatesWeights(List<Integer> possibleBoardStatesWeights) {
        System.out.println("newBoardPieceStateWeights: ");
        for (int i = 0; i < possibleBoardStatesWeights.size(); i++) {
            System.out.print(possibleBoardStatesWeights.get(i) + " ");
        }
        System.out.println();
    }

    public void printPieceAndSquare(List<PieceAndSquareTuple> pieceAndSquare) {
        System.out.println(" BoardStateGenerator; pieceAndSquare: Size: " + pieceAndSquare.size());
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

    public void printLegalMoves(List<Square> legalMoves) {
        System.out.print("BoardStateGenerator; Legal Moves: ");
        for (int i = 0; i < legalMoves.size(); i++) {
            System.out.print(" " + legalMoves.get(i).toString());
        }
        System.out.println();
    }

}
