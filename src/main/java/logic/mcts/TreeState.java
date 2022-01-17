package logic.mcts;

import logic.State;
import logic.board.Board;
import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.util.*;

import static logic.Dice.canMove;
import static logic.Dice.diceToPiece;
import static logic.enums.Piece.*;
import static logic.enums.Side.*;
import static logic.enums.Square.INVALID;
import static logic.mcts.Action.ActionType.*;

//TreeState is agnostic to dice roll
public class TreeState {

    Square whiteKing;
    Square blackKing;
    Square evilKing;

    Board board;
    Side playerToMove;
    Side winner;
    int score = 0;
    //int p;
    boolean terminal;
    int depth;
    int diceRoll;
    //not going to bother considering en-passant, or castling since they are statistically very unlikely to occur
    //the overhead of tracking/checking those, is not worth the value they would bring

    public TreeState(Board board, Side player, int depth) {
        this.board = board;
        this.depth = depth;
        playerToMove = player;
        //check if terminal state
        terminal = isTerminal();
//        for (int i = 0; i < 6; i++) {
//            score += Piece.values()[i].getWeight() * pieceCountPlayerToMove[i];
//            score -= Piece.values()[i].getWeight() * pieceCountOpponent[i];
//        }
        winner = NEUTRAL;
        evilKing = playerToMove == WHITE ? blackKing : whiteKing;
        //System.out.println("Evil King now at: "+ evilKing+ " player: " + playerToMove);
    }

    public ArrayList<Integer> getRolls() {
        ArrayList<Integer> validRolls = new ArrayList<>(6);
        State dummy = new State(board, playerToMove);

        for (int i = 0; i < 6; i++)
            if (canMove(diceToPiece[i].getColoredPiece(playerToMove), dummy)) {
                validRolls.add(i + 1); //valid dice rolls are in range 1-6, and i starts at 0
//                score += Piece.values()[i].getWeight() * pieceCountPlayerToMove[i];
//                score -= Piece.values()[i].getWeight() * pieceCountOpponent[i];
            }
//        else {
//                score += (i + 1) * pieceCountPlayerToMove[i];
//                score -= (i + 1) * pieceCountOpponent[i];
//            }

        Collections.shuffle(validRolls);

        if (validRolls.isEmpty()) {
            System.out.println("----------------------------\nNO VALID ROLLS?????");
            board.printBoard();
            System.out.println("player to move: " + playerToMove + "\nterminal? " + terminal + "\n------------------------------");
        }
        return validRolls;
    }

    public TreeState apply(Action action) {
        Board next = board.movePiece(action.origin, action.destination);
        if (action.type == PROMOTE || action.piece.canPromote(action.destination)) { //this or part is for captures into promotion rank
            next.setPiece(QUEEN.getColoredPiece(playerToMove), action.destination); //TODO: don't think this is correct
        }
        return new TreeState(next, Side.getOpposite(playerToMove), depth - 1);
    }

    public TreeState simulate(Action action) {
        //p *= -1;
        board = board.movePiece(action.origin, action.destination);
        if (action.type == PROMOTE || action.piece.canPromote(action.destination)) {
            board.setPiece(QUEEN.getColoredPiece(playerToMove), action.destination);
        }
        depth--;
        playerToMove = Side.getOpposite(playerToMove);
        terminal = isTerminal();
        evilKing = playerToMove == WHITE ? blackKing : whiteKing;
        return this;
    }

    static class ActionComparator implements Comparator<logic.mcts.Action> {

        @Override
        public int compare(logic.mcts.Action o1, logic.mcts.Action o2) {
            return (int) (o2.score - o1.score);
        }
    }

    public PriorityQueue<Action> getAvailableActions(int diceRoll) {
        //System.out.println("GETTING AVAILABLE ACTIONS..");
        this.diceRoll = diceRoll;
        PriorityQueue<Action> validActions = new PriorityQueue<>(new ActionComparator());
        Piece piece = diceToPiece[diceRoll - 1].getColoredPiece(playerToMove);
        Piece[] b = board.getBoard();

        for (int i = 0; i < b.length; i++) {
            Piece p = b[i];

            if (p != OFF_BOARD) {
                Square location = Square.getSquareByIndex(i);
                if (p == piece) {
                    double fuzz = Math.random();
                    switch (piece.getType()) {
                        //add score parameter to actions in order to put them in priority queue
                        //e.g. captures*value, protections, forwards, backwards, quiet
                        case KNIGHT, KING -> {
                            for (int offset : piece.getOffsets()) {
                                Square target = location.getOffSetSquare(offset);
                                if (target != Square.INVALID) {
                                    if (board.isEmpty(target)) {
                                        int manhattanDistance = Square.manhattanDistance(location, evilKing);
                                        int score = -3 * manhattanDistance + 60;
                                        if (p.getType() == KING) //Shy king heuristic
                                            score = (manhattanDistance > 6) ? score : manhattanDistance;
                                        validActions.add(new Action(p, location, target, score + fuzz, QUIET));
                                    } else if (!board.getPieceAt(target).isFriendly(playerToMove)) {
                                        Action.ActionType type = board.getPieceAt(target).getType() == KING ? WIN : CAPTURE;
//                                        Action capture =
//                                        int score =
                                        validActions.add(new Action(p, location, target, board.getPieceAt(target).getWeight(), type)); //capture
                                    }
                                }
                            }
                        }

                        case BISHOP, ROOK, QUEEN -> {
                            for (int offset : piece.getOffsets()) {
//                                Square target = Square.getSquare(location.getSquareNumber() + offset);
                                Square target = location.getOffSetSquare(offset);
                                while (target != INVALID) {
                                    if (board.isEmpty(target)) {
                                        validActions.add(new Action(p, location, target, -3 * Square.manhattanDistance(location, evilKing) + 60 + fuzz, QUIET)); //TODO manhattan distance
                                        target = target.getOffSetSquare(offset);
                                    } else if (!board.getPieceAt(target).isFriendly(playerToMove)) {
                                        Action.ActionType type = board.getPieceAt(target).getType() == KING ? WIN : CAPTURE;
                                        validActions.add(new Action(p, location, target, board.getPieceAt(target).getWeight(), type)); //capture
                                        break;
                                    } else {
                                        break;
                                    }
                                }

                            }
                        }
                        case PAWN -> {
                            //first check the natural move in the "forward" direction
                            Square naturalMove = location.getOffSetSquare(piece.getOffsets()[0]);
                            if (board.isEmpty(naturalMove)) {

                                //double jumping
                                Square doubleJump = naturalMove.getOffSetSquare(piece.getOffsets()[0]);
                                if (board.isEmpty(doubleJump) && piece.canDoubleJump(location)) {
                                    validActions.add(new Action(p, location, doubleJump, -3 * Square.manhattanDistance(location, evilKing) + 60 + fuzz, QUIET));
                                } else {
                                    int score = -3 * Square.manhattanDistance(location, evilKing) + 60;
                                    Action.ActionType type = QUIET;
                                    if (p.canPromote(naturalMove)) {
                                        score = QUEEN.getWeight() - 100;
                                        type = PROMOTE;
                                    } else if (p.promotable(naturalMove))
                                        score = QUEEN.getWeight() - 200;

                                    validActions.add(new Action(p, location, naturalMove, score + fuzz, type));
                                }
                            }

                            //now consider pawn capture moves which put the pawn in a different file
                            for (int k = 1; k < 3; k++) {
                                //loop through the two potential capture targets
                                Square captureTarget = location.getOffSetSquare(piece.getOffsets()[k]);
                                if (board.occupied(captureTarget)) {
                                    Piece atTarget = board.getPieceAt(captureTarget);
                                    if (atTarget.getColor().opponent(playerToMove)) {
                                        Action.ActionType type = atTarget.getType() == KING ? WIN : CAPTURE;
                                        int score = (p.canPromote(captureTarget)) ? atTarget.getWeight() + QUEEN.getWeight() : atTarget.getWeight();
                                        validActions.add(new Action(p, location, captureTarget, score + fuzz, type)); //capture
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                i += 7;
            }
        }
        return validActions;
    }

    //int[] pieceCountOpponent = new int[7];
    //int[] pieceCountPlayerToMove = new int[7];


    //optimizable with piece list / concurrent traversal of board
    public boolean isTerminal() {
        //System.out.println("CHECKING TERMINAL...");
        Piece[] b = board.getBoard();
        boolean whiteKing = false, blackKing = false, terminal = true;
        for (int i = 0; i < b.length; i++) {
            if (b[i] == OFF_BOARD) {
                i += 7;
            } else if (b[i] != EMPTY) {
                //if (b[i].getColor().opponent(playerToMove)) pieceCountOpponent[b[i].getType().ordinal()]++;
                //else pieceCountPlayerToMove[b[i].getType().ordinal()]++;

                if (b[i] == WHITE_KING) {
                    whiteKing = true;
                    this.whiteKing = Square.getBoardIndexMap().get(i);
                } else if (b[i] == BLACK_KING) {
                    blackKing = true;
                    this.blackKing = Square.getBoardIndexMap().get(i);
                }

                if (whiteKing && blackKing) { //both kings on the board, so not terminal
                    //System.out.println("TERMINAL FALSE");
                    return false;
                    //terminal = false;
                }
            }
        }

        //if (!terminal) return false;

        if (playerToMove == WHITE && !whiteKing) {
            //if WHITE is to move, but there is no white King, then black captured the white king
            winner = BLACK;
        } else if (playerToMove == BLACK && !blackKing) {
            winner = WHITE;
        } else {
            System.out.println("NEITHER WHITE NOR BLACK WON THE GAME?");
        }
        //System.out.println("TERMINAL TRUE");
        return true; //either one of the kings is not on the board
    }

    public double reward(Side player) {
        return winner.opponent(player) ? 1 : 0;
    }

    public static void main(String[] args) {
        String fen = "rnbqk2r/pppp1ppp/4pn2/8/1bPP4/2N2N2/PP2PPPP/R1BQKB1R b KQkq - 3 4";
        TreeState ts = new TreeState(new Board0x88(fen), WHITE, 0);
        //TreeState ts = new TreeState(new Board0x88(Config.OPENING_FEN), WHITE, 0);
        //System.out.println(Arrays.toString(ts.board.getBoard()));
        ts.board.printBoard();
        System.out.println("Input array: " + Arrays.toString(ts.normalizedBoardArray()));
    }

    public int[] normalizedBoardArray() {
        int[] b = new int[70]; //all boards + all piece types
        int index = 0;
        Piece[] x88 = board.getBoard();

        for (int i = 0; i < x88.length; i++) {
            if (x88[i] == OFF_BOARD) {
                i += 7;
            } else {
                b[index++] = x88[i].getPiecePerspective(playerToMove);
            }
        }

        /*
        To add the dice roll at the correct index:
              index = 64 + roll - 1

        For example:
            roll = 5;
            b[64 + roll - 1] = 1;

        This sets the correct array position corresponding to the dice roll (piece type)
        */
        return b;
    }
}
