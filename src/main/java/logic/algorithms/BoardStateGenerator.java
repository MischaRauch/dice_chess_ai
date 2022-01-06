package logic.algorithms;

import com.twelvemonkeys.io.SeekableOutputStream;
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

import static logic.enums.Piece.EMPTY;
import static logic.enums.Square.*;

// generates all possible states of the board for n turns ahead
public class BoardStateGenerator {

    // for ML
    public static ArrayList<State> getPossibleBoardStates(State state, Side side) {
        ArrayList<OriginAndDestSquare> originAndDestSquares = LegalMoveGenerator.getAllLegalMoves(state, side);
        ArrayList<State> answer = new ArrayList<State>();
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
    // for QL eval, could be made more efficient
    public static ArrayList<State> getPossibleBoardStatesOfSpecificPiece(State state, Side side, int diceroll) {
        ArrayList<OriginAndDestSquare> originAndDestSquares = LegalMoveGenerator.getAllLegalMoves(state, side);
        ArrayList<State> answer = new ArrayList<State>();
        State tempState;
        Move move1;

        for (OriginAndDestSquare tempMove : originAndDestSquares) {
            Piece p = state.getBoard().getPieceAt(tempMove.getOrigin());

            if (p == Piece.getPieceFromDice(diceroll, side)) {
                move1 = new Move(p, tempMove.getOrigin(), tempMove.getDest(), diceroll, side);
                tempState = state.applyMove(move1);
                answer.add(tempState);
            }
        }
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
        LegalMoveGenerator generator = new LegalMoveGenerator();

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
    // getPossibleBoardStatesWeightsOfSpecificPiece(parentNode.getPreviousState().getPieceAndSquare(), parentNode.getPreviousState().getColor(), i, parentNode.getPreviousState());
    // gets a list of all the possible board weights for specific piece for all the possible board states List<PieceAndSquareTuple> nodePieceAndSquare type (i.e. WHITE_PAWN)
    public List<Integer> getPossibleBoardStatesWeightsOfSpecificPiece(List<PieceAndSquareTuple> nodePieceAndSquare, Side color, int diceRoll, State state) {
        List<Integer> possibleBoardStatesWeights = new ArrayList<Integer>();

        boolean applyQL = true;
        int depth = 2;

        if (applyQL == true) {
            int a = 0;
            ArrayList<State> allPossibleStates = getPossibleBoardStatesOfSpecificPiece(state, color, diceRoll);

            for (State givenState : allPossibleStates) {
                int weight = BoardStateEvaluator.getEvalOfQL(givenState, depth); // where to put this
                possibleBoardStatesWeights.add(weight);
            }
            System.out.println("a");
            return possibleBoardStatesWeights;
        }
        else {
            List<List<PieceAndSquareTuple>> possibleBoardStates = getPossibleBoardStates(nodePieceAndSquare, color, diceRoll, state);

            for (List<PieceAndSquareTuple> boardState : possibleBoardStates) {
                int newBoardPieceStateWeights = BoardStateEvaluator.getBoardEvaluationNumber(boardState, color, state.getCumulativeTurn());
                //System.out.println("newBoardPieceStateWeights: " + newBoardPieceStateWeights);
                possibleBoardStatesWeights.add(newBoardPieceStateWeights);
            }
            //printPossibleBoardStatesWeights(possibleBoardStatesWeights);
            return possibleBoardStatesWeights;
        }
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
