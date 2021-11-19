package logic.expectiminimax;

import logic.LegalMoveGenerator;
import logic.PieceAndSquareTuple;
import logic.PieceAndTurnDeathTuple;
import logic.State;
import logic.board.Board;
import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.game.Game;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static logic.enums.Side.WHITE;

// generates all possible states of the board for n turns ahead
public class BoardStateGenerator {

    //game independent

    // move piece and return array
    public List<List<PieceAndSquareTuple>> getStateFromLegalMoves(List<PieceAndSquareTuple> nodePieceAndSquare, List<Square>legalMoves, Piece piece, Square origin) {

        List<List<PieceAndSquareTuple>> possibleStates = new ArrayList<>();

        List<PieceAndSquareTuple> nodePieceAndSquareCopy = (List<PieceAndSquareTuple>) nodePieceAndSquare.stream().collect(Collectors.toList());
        List<PieceAndSquareTuple> nodePieceAndSquareCopy2 = (List<PieceAndSquareTuple>) nodePieceAndSquare.stream().collect(Collectors.toList());
        List<PieceAndSquareTuple> nodePieceAndSquareCopy3 = (List<PieceAndSquareTuple>) nodePieceAndSquare.stream().collect(Collectors.toList());
        List<PieceAndSquareTuple> nodePieceAndSquareCopy4 = (List<PieceAndSquareTuple>) nodePieceAndSquare.stream().collect(Collectors.toList());

        // loop through state
        for (PieceAndSquareTuple t : nodePieceAndSquareCopy) {
            // you know you are at origin
            if (t.getSquare() == origin && t.getPiece() == piece) {
                // loop through legal moves
                for (int i = 0; i < legalMoves.size(); i++) {

                    // update piece and square

                    // remove previous location
                    for (PieceAndSquareTuple t2 : nodePieceAndSquareCopy2) {
                        if (t2.getPiece() == piece && t2.getSquare() == origin) {
                            nodePieceAndSquareCopy3.remove(t2);
                        }
                    }

                    // add new location
                    nodePieceAndSquareCopy3.add(new PieceAndSquareTuple(piece, legalMoves.get(i)));

                    // remove pieces at possible new location
                    for (PieceAndSquareTuple t2 : nodePieceAndSquareCopy2) {
                        // never get legal move that goes on same square as friendly piece, assume always different piece (eg. [PIECE]_BLACK if AI is [PIECE]_WHITE)
                        if (t2.getPiece() != piece && t2.getSquare() == legalMoves.get(i)) {
                            nodePieceAndSquareCopy3.remove(t2);
                        }
                    }

                    // add updated pieceAndSquare (state) for (Squares) legalmoves.get(i) to list
                    //possibleStates.put((Piece)t.getPiece(),nodePieceAndSquareCopy3);
                    possibleStates.add(nodePieceAndSquareCopy3);

                    // reset
                    nodePieceAndSquareCopy3 = (List<PieceAndSquareTuple>) nodePieceAndSquareCopy4.stream().collect(Collectors.toList());
                }
            }

        }
//        for (int i = 0; i < possibleStates.size(); i++) {
//            System.out.print("Piece: " + possibleStates.get(i).get(0).getPiece().toString());
//            printPieceAndSquare(possibleStates.get(i));
//        }
        return possibleStates;
    }

    // list of piece states for given dice roll and color
    public List<List<PieceAndSquareTuple>> getPossibleBoardStates(List<PieceAndSquareTuple> nodePieceAndSquare, Side color, int diceRoll, State state) {
        List<PieceAndSquareTuple> nodePieceAndSquareCopy = (List<PieceAndSquareTuple>) nodePieceAndSquare.stream().collect(Collectors.toList());
        List<PieceAndSquareTuple> nodePieceAndSquareCopy2 = (List<PieceAndSquareTuple>) nodePieceAndSquare.stream().collect(Collectors.toList());

        List<List<PieceAndSquareTuple>> possibleStates = new ArrayList<>();

        printArray(nodePieceAndSquareCopy);

        for (PieceAndSquareTuple t : nodePieceAndSquareCopy) {
            // clone for casting
            Piece p = (Piece) t.getPiece();
            Square s = (Square) t.getSquare(); //origin
            // get all piece types with their dice numbers

//            List<Integer> diceNumbers = new ArrayList<>();
//            for (PieceAndSquareTuple tDice : nodePieceAndSquareCopyDiceNumbers) {
//                //friendly pieces
//                if(Piece.getColorOfPiece((Piece)tDice.getPiece())==state.color) {
//                    int dice = Piece.getDiceFromPiece((Piece)tDice.getPiece());
//                    if(!diceNumbers.contains(dice)) {
//                        diceNumbers.add(dice);
//                    }
//                }
//            }

            //System.out.println("BoardStateGenerator; Dice numbers: " + diceNumbers);

            // loop for each dice roll number and generate states and add states

            // if friendly
//            for (int i = 0; i < diceNumbers.size(); i++) {
//                if (p == Piece.getPieceFromDice(diceNumbers.get(i), color)) {
//                   System.out.println("\nPiece: " + p.toString());
                    Piece coloredPiece = Piece.getPieceFromDice(diceRoll,color);
                    if (p==coloredPiece) {
                        List<Square> legalMoves = LegalMoveGenerator.getLegalMoves(state, s, p, color);
                        printLegalMoves(legalMoves);

                        List<List<PieceAndSquareTuple>> states = getStateFromLegalMoves(nodePieceAndSquareCopy2,legalMoves,p,s);

                        for (int j = 0; j < states.size(); j++) {
                            possibleStates.add(states.get(j));
                            //printPieceAndSquare(states.get(j));
                        }
                    }
//                }
//            }
        }

        for (int i = 0; i < possibleStates.size(); i++) {
            printPieceAndSquare(possibleStates.get(i));
        }
        return possibleStates;
    }

    public List<Integer> getPossibleBoardStatesWeights(List<PieceAndSquareTuple> nodePieceAndSquare, Side color, int diceRoll, State state) {
        List<Integer> possibleBoardStatesWeights = new ArrayList<Integer>();
//        Map<Piece,Integer> possibleBoardStatesWeights = new HashMap<>();

        List<List<PieceAndSquareTuple>> possibleBoardStates = getPossibleBoardStates(nodePieceAndSquare,color,diceRoll,state);

//        for (Piece p : possibleBoardStates.keySet()) {
//            int newBoardPieceStateWeights = BoardStateEvaluator.getBoardEvaluationNumber(possibleBoardStates.get(p), color, diceRoll);
//            possibleBoardStatesWeights.put(Piece.getPieceFromDice(diceRoll,color), newBoardPieceStateWeights);
//        }
//
        for (List<PieceAndSquareTuple> s : possibleBoardStates) {
            int newBoardPieceStateWeights = BoardStateEvaluator.getBoardEvaluationNumber(s, color, diceRoll);
            possibleBoardStatesWeights.add(newBoardPieceStateWeights);
        }
        return possibleBoardStatesWeights;
    }

    public void printPieceAndSquare(List<PieceAndSquareTuple> pieceAndSquare){
        System.out.println(" BoardStateGenerator; pieceAndSquare: Size: " + pieceAndSquare.size());
        for (PieceAndSquareTuple t : pieceAndSquare) {
            System.out.print(t.toString() + " | ");
        }
        printPieceCounts(pieceAndSquare);
    }

    private void printPieceCounts(List<PieceAndSquareTuple> pieceAndSquare) {
        int pawn = 0;
        int knight = 0;
        int rook = 0;
        int bishop = 0;
        int king = 0;
        int queen = 0;
        for (PieceAndSquareTuple t : pieceAndSquare) {
            if(t.getPiece().equals(Piece.BLACK_QUEEN)||t.getPiece().equals(Piece.WHITE_QUEEN)) {
                queen++;
            }else if (t.getPiece().equals(Piece.WHITE_BISHOP)||t.getPiece().equals(Piece.BLACK_BISHOP)) {
                bishop++;
            }else if (t.getPiece().equals(Piece.WHITE_KING)||t.getPiece().equals(Piece.BLACK_KING)) {
                king++;
            }else if (t.getPiece().equals(Piece.WHITE_ROOK)||t.getPiece().equals(Piece.BLACK_ROOK)) {
                rook++;
            }else if (t.getPiece().equals(Piece.WHITE_PAWN)||t.getPiece().equals(Piece.BLACK_PAWN)) {
                pawn++;
            }else if (t.getPiece().equals(Piece.WHITE_KNIGHT)||t.getPiece().equals(Piece.BLACK_KNIGHT)) {
                knight++;
            }
        }
        System.out.println("\nCounts: Pawn: " + pawn + " Knight: " + knight + " Bishop: " + bishop + " Rook: " + rook + " Queen: " + queen + " King: " + king + "\n");
    }

    private Piece[][] flipMatrixHorizontal(Piece[][] matrix) {
        Piece temp;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length / 2; j++) {
                temp = matrix[i][j];
                matrix[i][j] = matrix[i][matrix.length - 1 - j];
                matrix[i][matrix.length - 1 -j] = temp;
            }
        }
        return matrix;
    }

    private void printLegalMoves(List<Square> legalMoves) {
        System.out.print("BoardStateGenerator; Legal Moves: ");
        for (int i = 0; i < legalMoves.size(); i++) {
            System.out.print(" " + legalMoves.get(i).toString());
        }
        System.out.println();
    }

    public void printArray(List<PieceAndSquareTuple> pieceAndSquare){
        System.out.println("BoardStateGenerator; printArray: ");
        for (PieceAndSquareTuple t : pieceAndSquare) {
            System.out.print(t.toString() + " | ");
        }
        System.out.println("Size: " + pieceAndSquare.size());
    }

}
