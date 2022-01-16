package logic.algorithms;

import logic.LegalMoveGenerator;
import logic.ML.OriginAndDestSquare;
import logic.Move;
import logic.PieceAndSquareTuple;
import logic.State;
import logic.board.Board;
import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static logic.State.PieceAndSquareToBoardConverter;
import static logic.enums.Piece.*;
import static logic.enums.Square.*;

// generates all possible states of the board for n turns ahead
public class BoardStateGenerator {

    // for ML
    public static ArrayList<State> getPossibleBoardStates(State state, Side side) {
        ArrayList<OriginAndDestSquare> originAndDestSquares = LegalMoveGenerator.getAllLegalMovesML(state, side);
        ArrayList<State> answer = new ArrayList<>();
        State tempState;
        Move move1;

        for (OriginAndDestSquare tempMove : originAndDestSquares) {
            Piece p = state.getBoard().getPieceAt(tempMove.getOrigin());

            move1 = new Move(p, tempMove.getOrigin(), tempMove.getDest(), Piece.getDiceFromPiece(p), side);
            tempState = state.applyMove(move1);
            answer.add(tempState);
        }
        return answer;
    }
    public static ArrayList<State> getPossibleBoardStatesOfSpecificPiece(State state, Side side, int roll) {
        ArrayList<OriginAndDestSquare> originAndDestSquares = LegalMoveGenerator.getAllLegalMovesML(state, side);

        ArrayList<State> answer = new ArrayList<>();
        State tempState;
        Move move1;

        for (OriginAndDestSquare tempMove : originAndDestSquares) {
            Piece p = state.getBoard().getPieceAt(tempMove.getOrigin());
            if (p.getColor().equals(side) && getDiceFromPiece(p) == roll) {
                move1 = new Move(p, tempMove.getOrigin(), tempMove.getDest(), Piece.getDiceFromPiece(p), side);
                tempState = state.applyMove(move1);
                answer.add(tempState);
            }
        }
        System.out.println(answer.size());
        return answer;
    }


    public List<Move> getValidMovesForGivenPiece(State state, Piece piece) {
        List<Move> validMoves = new LinkedList<>();

        Board board = state.getBoard();
        Board0x88 b = (Board0x88) board;

        //TODO: use piece lists so we don't have to loop through entire board
        //TODO: somewhere we need to detect check (both enemy and ours)
        //TODO detect which moves protect the king/valuable pieces from check
        //TODO: avoid moving into squares which are under attack. Maybe depending on how many available pieces opponent has
        //TODO: add move option to promote pawn despite dice roll -> need way to see if advantageous to do so
        for (int i = 0; i < b.getBoardArray().length; i++) {
            Piece p = b.getBoardArray()[i];
            Square location = Square.getSquareByIndex(i);

            if (p == piece) {
                switch (piece.getType()) {
                    case PAWN -> {
                        //first check the natural move in the "forward" direction
                        Square naturalMove = Square.getSquare(location.getSquareNumber() + piece.getOffsets()[0]);
                        if (naturalMove != Square.INVALID && board.isEmpty(naturalMove)) {
                            Move natural = new Move(p, location, naturalMove, state.getDiceRoll(), piece.getColor());

                            //promote if possible
                            if (p.canPromote(naturalMove)) {
                                //pawn is moving into promotion rank
                                if (state.getDiceRoll() != 1 && state.getDiceRoll() != 6) {
                                    //if dice roll is not pawn or king, then automatically promote piece
                                    natural.promotionPiece = p.promote(state.getDiceRoll());
                                } else {
                                    //auto promote to Queen
                                    //TODO: potentially give AI the choice to promote to knight in cases where it would lead to a king capture in the next turn
                                    natural.promotionPiece = Piece.QUEEN.getColoredPiece(piece.getColor());
                                }
                                natural.promotionMove = true;
                            }
                            validMoves.add(natural);

                            //double jumping
                            Square doubleJump = Square.getSquare(naturalMove.getSquareNumber() + piece.getOffsets()[0]);
                            if (doubleJump != Square.INVALID && board.isEmpty(doubleJump) && piece.canDoubleJump(location)) {
                                Move move = new Move(p, location, doubleJump, state.getDiceRoll(), piece.getColor());
                                //set en-passant stuff
                                move.setEnPassant(naturalMove);
                                move.setEnPassantMove(true);
                                validMoves.add(move);
                            }
                        }

                        //now consider pawn capture moves which put the pawn in a different file
                        for (int k = 1; k < 3; k++) {
                            //loop through the two potential capture targets
                            Square captureTarget = Square.getSquare(location.getSquareNumber() + piece.getOffsets()[k]);

                            //only consider valid squares. Not sure if necessary since it's technically impossible for a pawn to be in the last rank
                            //since it would immediately be promoted, but let's just be safe.
                            if (captureTarget != Square.INVALID) {
                                Piece atTarget = b.getPieceAt(captureTarget);

                                if (atTarget == EMPTY) {
                                    //no piece at target, but en-passant capture still possible
                                    if (state.getEnPassant() != INVALID) {
                                        Move capture = new Move(p, location, captureTarget, state.getDiceRoll(), piece.getColor());
                                        capture.setEnPassantCapture(true);
                                        validMoves.add(capture);
                                    }
                                } else {
                                    //capture target square is occupied by enemy piece
                                    if (!atTarget.isFriendly(piece.getColor())) {
                                        Move capture = new Move(p, location, captureTarget, state.getDiceRoll(), piece.getColor());

                                        //check if capture results in promotion and promote if so
                                        if (p.canPromote(captureTarget)) {
                                            //pawn is moving into promotion rank
                                            if (state.getDiceRoll() != 1 && state.getDiceRoll() != 6) {
                                                //if dice roll is not pawn or king, then automatically promote piece
                                                capture.promotionPiece = p.promote(state.getDiceRoll());
                                            } else {
                                                //auto promote to Queen
                                                //TODO: potentially give AI the choice to promote to knight in cases where it would lead to a king capture in the next turn
                                                capture.promotionPiece = Piece.QUEEN.getColoredPiece(piece.getColor());
                                            }

                                            capture.promotionMove = true;
                                        }
                                        //add capture move to the list of valid moves
                                        validMoves.add(capture);
                                    }
                                }
                            }

                        }
                    }

                    case KNIGHT -> {
                        for (int offset : piece.getOffsets()) {
                            Square target = Square.getSquare(location.getSquareNumber() + offset);
                            if (target != Square.INVALID) {
                                if (board.isEmpty(target) || !board.getPieceAt(target).isFriendly(piece.getColor()))
                                    validMoves.add(new Move(p, location, target, state.getDiceRoll(), piece.getColor()));
                            }
                        }
                    }

                    case KING -> {
                        for (int offset : piece.getOffsets()) {
                            if (!board.isOffBoard(location.getSquareNumber() + offset)) {
                                Square target = Square.getSquare(location.getSquareNumber() + offset);

                                if (board.isEmpty(target) || !board.getPieceAt(target).isFriendly(piece.getColor())) {
                                    validMoves.add(new Move(p, location, target, state.getDiceRoll(), piece.getColor()));
                                }
                            }
                        }
                        //CHECK FOR CASTLING
                        //TODO: check if White can castle before generating the move
                        if (piece.getColor() == Side.WHITE) {
                            if (location == Square.e1) {
                                //SHORT WHITE
                                if (board.isEmpty(location.getSquareRight()) && board.isEmpty(getSquare(6)) && state.isShortCastlingWhite()) {
                                    Move m = new Move(p, location, Square.g1, state.getDiceRoll(), piece.getColor());
                                    m.castlingRookDestination = f1;
                                    validMoves.add(m);
                                    //TODO: add this type of move gen with flags to all other castling thingies
                                }
                                //LONG WHITE
                                if (board.isEmpty(location.getSquareLeft()) && board.isEmpty(getSquare(2)) && board.isEmpty(getSquare(1)) && state.isLongCastlingWhite()) {
                                    Move m = new Move(p, location, Square.c1, state.getDiceRoll(), piece.getColor());
                                    m.castlingRookDestination = d1;
                                    validMoves.add(m);
                                }
                            }
                            //TODO: check if black can castle before generating the move
                        } else {
                            if (location.getSquareNumber() == 116) {
                                //SHORT BLACK
                                if (board.isEmpty(location.getSquareRight()) && board.isEmpty(getSquare(118)) && state.isShortCastlingBlack()) {
                                    Move m = new Move(p, location, Square.g8, state.getDiceRoll(), piece.getColor());
                                    m.castlingRookDestination = f8;
                                    validMoves.add(m);

                                }
                                //LONG BLACK
                                if (board.isEmpty(location.getSquareLeft()) && board.isEmpty(getSquare(114)) && board.isEmpty(getSquare(113)) && state.isLongCastlingBlack()) {
                                    Move m = new Move(p, location, Square.c8, state.getDiceRoll(), piece.getColor());
                                    m.castlingRookDestination = d8;
                                    validMoves.add(m);
                                }
                            }
                        }
                    }

                    //TODO: Rook case should avoid moving the unmoved rook if castling on that side is still possible
                    case BISHOP, ROOK, QUEEN -> {
                        for (int offset : piece.getOffsets()) {
                            Square target = Square.getSquare(location.getSquareNumber() + offset);

                            while (target != INVALID && board.isEmpty(target)) {
                                validMoves.add(new Move(p, location, target, state.getDiceRoll(), piece.getColor()));
                                target = Square.getSquare(target.getSquareNumber() + offset);
                            }

                            if (target != INVALID && !board.getPieceAt(target).isFriendly(piece.getColor())) {
                                validMoves.add(new Move(p, location, target, state.getDiceRoll(), piece.getColor()));
                            }
                        }
                    }
                }
            }

        }

        return validMoves;
    }
    // add the effects of castling, promotion, en passant (this is not that urgent, because it's rare) here
    public static List<List<PieceAndSquareTuple>> getStateFromLegalMoves(List<PieceAndSquareTuple> nodePieceAndSquare, List<Square> legalMoves, Piece piece, Square origin) {

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

//                    if ((piece == WHITE_PAWN || piece == BLACK_PAWN) && ((legalMoves.get(i).getRank() == 1) || (legalMoves.get(i).getRank() == 8))) {
//                        if ((piece == WHITE_PAWN) && legalMoves.get(i).getRank() == 8) {
//                            nodePieceAndSquareCopy2.add(new PieceAndSquareTuple(WHITE_QUEEN, legalMoves.get(i)));
//                        } else if ((piece == BLACK_PAWN) && legalMoves.get(i).getRank() == 1) {
//                            nodePieceAndSquareCopy2.add(new PieceAndSquareTuple(BLACK_QUEEN, legalMoves.get(i)));
//                        }
//                    } else if ((piece == WHITE_KING || piece == BLACK_KING)) {
//                        if (piece == WHITE_KING && (origin.getSquareNumber() == 4) && legalMoves.get(i).getSquareNumber() == 6) {
//                            nodePieceAndSquareCopy2.add(new PieceAndSquareTuple(WHITE_KING, legalMoves.get(i)));
//
//                            for (PieceAndSquareTuple temp : nodePieceAndSquareCopy) {
//                                if (temp.getPiece() == WHITE_ROOK && temp.getSquare() == h1) {
//                                    nodePieceAndSquareCopy2.remove(temp);
//                                }
//                            }
//                            nodePieceAndSquareCopy2.add(new PieceAndSquareTuple(WHITE_ROOK, f1));
//                        } else if (piece == WHITE_KING && (origin.getSquareNumber() == 4) && legalMoves.get(i).getSquareNumber() == 2) {
//                            nodePieceAndSquareCopy2.add(new PieceAndSquareTuple(WHITE_KING, legalMoves.get(i)));
//                            for (PieceAndSquareTuple temp : nodePieceAndSquareCopy) {
//                                if (temp.getPiece() == WHITE_ROOK && temp.getSquare() == a1) {
//                                    nodePieceAndSquareCopy2.remove(temp);
//                                }
//                            }
//                            nodePieceAndSquareCopy2.add(new PieceAndSquareTuple(WHITE_ROOK, d1));
//                        } else if (piece == BLACK_KING && (origin.getSquareNumber() == 116) && legalMoves.get(i).getSquareNumber() == 118) {
//                            nodePieceAndSquareCopy2.add(new PieceAndSquareTuple(BLACK_KING, legalMoves.get(i)));
//                            for (PieceAndSquareTuple temp : nodePieceAndSquareCopy) {
//                                if (temp.getPiece() == BLACK_ROOK && temp.getSquare() == h8) {
//                                    nodePieceAndSquareCopy2.remove(temp);
//                                }
//                            }
//                            nodePieceAndSquareCopy2.add(new PieceAndSquareTuple(BLACK_ROOK, f8));
//                        } else if (piece == BLACK_KING && (origin.getSquareNumber() == 116) && legalMoves.get(i).getSquareNumber() == 114) {
//                            nodePieceAndSquareCopy2.add(new PieceAndSquareTuple(BLACK_KING, legalMoves.get(i)));
//                            for (PieceAndSquareTuple temp : nodePieceAndSquareCopy) {
//                                if (temp.getPiece() == BLACK_ROOK && temp.getSquare() == a8) {
//                                    nodePieceAndSquareCopy2.remove(temp);
//                                }
//                            }
//                            nodePieceAndSquareCopy2.add(new PieceAndSquareTuple(BLACK_ROOK, d8));
//                        }
//                    } else {
//                        nodePieceAndSquareCopy2.add(new PieceAndSquareTuple(piece, legalMoves.get(i)));
//                    }

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

        for (PieceAndSquareTuple<Piece,Square> t : nodePieceAndSquareCopy) {
            // clone for casting
            // get all piece types with their dice numbers
            Piece coloredPiece = Piece.getPieceFromDice(diceRoll, color);
            if (t.getPiece() == coloredPiece) {
                List<Square> legalMoves = LegalMoveGenerator.getMoves(state, t.getSquare(), t.getPiece());

                List<List<PieceAndSquareTuple>> states = getStateFromLegalMoves(nodePieceAndSquareCopy2, legalMoves, t.getPiece(), t.getSquare());

                for (int j = 0; j < states.size(); j++) {
                    possibleStates.add(states.get(j));
                }
            }
        }
        return possibleStates;
    }
    // for hybrid
    public static List<List<PieceAndSquareTuple>> getPossibleBoardStates(List<PieceAndSquareTuple> nodePieceAndSquare, Side color, int diceRoll) {
        List<PieceAndSquareTuple> nodePieceAndSquareCopy = nodePieceAndSquare.stream().collect(Collectors.toList());
        List<PieceAndSquareTuple> nodePieceAndSquareCopy2 = nodePieceAndSquare.stream().collect(Collectors.toList());
        List<List<PieceAndSquareTuple>> possibleStates = new ArrayList<>();

        for (PieceAndSquareTuple t : nodePieceAndSquareCopy) {
            // clone for casting
            Piece p = (Piece) t.getPiece();
            Square s = (Square) t.getSquare(); //origin
            Piece coloredPiece = Piece.getPieceFromDice(diceRoll, color);
            if (p == coloredPiece) {
                // List<Square> legalMoves = LegalMoveGenerator.getLegalMoves(state, s, p, color);
                List<Square> legalMoves = LegalMoveGenerator.getLegalMovesHybrid(nodePieceAndSquareCopy, s, p, color);

                List<List<PieceAndSquareTuple>> states = getStateFromLegalMoves(nodePieceAndSquareCopy2, legalMoves, p, s);
                possibleStates.addAll(states);
            }
        }
        return possibleStates;
    }

//    // gets a list of all the possible board weights for specific piece for all the possible board states List<PieceAndSquareTuple> nodePieceAndSquare type (i.e. WHITE_PAWN)
//    public List<Integer> getPossibleBoardStatesWeightsOfSpecificPiece(List<PieceAndSquareTuple> nodePieceAndSquare, Side color, int diceRoll, State state) {
//
//        List<List<PieceAndSquareTuple>> possibleBoardStates = getPossibleBoardStates(nodePieceAndSquare, color, diceRoll, state);
//
//        List<Integer> possibleBoardStatesWeights = new ArrayList<Integer>();
//
//        for (List<PieceAndSquareTuple> boardState : possibleBoardStates) {
//            int newBoardPieceStateWeights = BoardStateEvaluator.getBoardEvaluationNumber(boardState, color, state.getCumulativeTurn());
//            possibleBoardStatesWeights.add(newBoardPieceStateWeights);
//        }
//        return possibleBoardStatesWeights;
//    }

//    // gets a list of all the possible board weights for specific piece for all the possible board states List<PieceAndSquareTuple> nodePieceAndSquare type (i.e. WHITE_PAWN)
//    public List<Integer> getPossibleBoardStatesWeightsOfSpecificPiece(List<PieceAndSquareTuple> nodePieceAndSquare, Side color, int diceRoll, State state) {
//
//        List<List<PieceAndSquareTuple>> possibleBoardStates = getPossibleBoardStates(nodePieceAndSquare, color, diceRoll, state);
//
//        List<Integer> possibleBoardStatesWeights = new ArrayList<Integer>();
//
//        for (List<PieceAndSquareTuple> boardState : possibleBoardStates) {
//            int newBoardPieceStateWeights = BoardStateEvaluator.getBoardEvaluationNumber(boardState, color, state.getCumulativeTurn());
//            possibleBoardStatesWeights.add(newBoardPieceStateWeights);
//        }
//        return possibleBoardStatesWeights;
//    }

    // gets a list of all the possible board weights for specific piece for all the possible board states List<PieceAndSquareTuple> nodePieceAndSquare type (i.e. WHITE_PAWN)
    public List<Integer> getPossibleBoardStatesWeightsOfSpecificPiece(List<List<PieceAndSquareTuple>> possibleBoardStatesForGivenPiece, Side color, int turn) {

        List<Integer> possibleBoardStatesWeights = new ArrayList<Integer>();

        for (List<PieceAndSquareTuple> boardState : possibleBoardStatesForGivenPiece) {
            int newBoardPieceStateWeights = BoardStateEvaluator.getBoardEvaluationNumber(boardState, color, turn);
            possibleBoardStatesWeights.add(newBoardPieceStateWeights);
        }
        return possibleBoardStatesWeights;
    }

    // gets a list of all the possible board weights for specific piece for all the possible board states List<PieceAndSquareTuple> nodePieceAndSquare type (i.e. WHITE_PAWN)
    public List<Integer> getPossibleBoardStatesWeightsOfSpecificPieceHybrid(Side color, int diceRoll, State state) {
        List<Integer> possibleBoardStatesWeights = new ArrayList<Integer>();

        int depth = 2;

        ArrayList<State> allPossibleStates = getPossibleBoardStatesOfSpecificPiece(new State(state), color, diceRoll);

        for (State givenState : allPossibleStates) {
            int weight = BoardStateEvaluator.getEvalOfQL(new State(givenState), depth); // the location of this causes too much time
            possibleBoardStatesWeights.add(weight);
        }

//        } else {
//            List<List<PieceAndSquareTuple>> possibleBoardStates = getPossibleBoardStates(nodePieceAndSquare, color, diceRoll, state);
//
//            for (List<PieceAndSquareTuple> boardState : possibleBoardStates) {
//                int newBoardPieceStateWeights = BoardStateEvaluator.getBoardEvaluationNumber(boardState, color, state.getCumulativeTurn());
//                //System.out.println("newBoardPieceStateWeights: " + newBoardPieceStateWeights);
//                possibleBoardStatesWeights.add(newBoardPieceStateWeights);
//            }
//            //printPossibleBoardStatesWeights(possibleBoardStatesWeights);
//        }
        return possibleBoardStatesWeights;
    }
    

    public void printPossibleBoardStatesWeights(List<Integer> possibleBoardStatesWeights) {
        // System.out.println("newBoardPieceStateWeights: ");
        for (int i = 0; i < possibleBoardStatesWeights.size(); i++) {
            // System.out.print(possibleBoardStatesWeights.get(i) + " ");
        }
        // System.out.println();
    }

    public void printPieceAndSquare(List<PieceAndSquareTuple> pieceAndSquare) {
        // System.out.println(" BoardStateGenerator; pieceAndSquare: Size: " + pieceAndSquare.size());
        for (PieceAndSquareTuple t : pieceAndSquare) {
            // System.out.print(t.toString() + " | ");
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
        // System.out.println();
    }

}
