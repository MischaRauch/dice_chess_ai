package logic.mcts;

import logic.Config;
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
import static logic.enums.Side.BLACK;
import static logic.enums.Side.WHITE;
import static logic.enums.Square.INVALID;
import static logic.mcts.Action.ActionType.*;

//TreeState is agnostic to dice roll
public class TreeState {

    Square whiteKing;
    Square blackKing;
    Square evilKing;

    Board board;
    Side playerToMove;
    //int p;
    boolean terminal;
    int depth;
    //not going to bother considering en-passant, or castling since they are statistically very unlikely to occur
    //the overhead of tracking/checking those, is not worth the value they would bring

    public TreeState(Board board, Side player, int depth) {
        this.board = board;
        this.depth = depth;
        playerToMove = player;
        //p = player == Side.WHITE ? 1 : -1;
        //System.out.println("Old evil king: "+ evilKing + " player: " + playerToMove);
        //check if terminal state
        terminal = isTerminal();
        evilKing = playerToMove == WHITE ? blackKing : whiteKing;
        //System.out.println("Evil King now at: "+ evilKing+ " player: " + playerToMove);
    }
//    public TreeState(Board board, Side player, int depth, Square evilKing) {
//        this.board = board;
//        this.depth = depth;
//        playerToMove = player;
//        terminal = isTerminal();
//        this.evilKing = evilKing;
//    }

    public List<Integer> getRolls() {
        List<Integer> validRolls = new ArrayList<>(6);
        State dummy = new State(board, playerToMove);

        for (int i = 0; i < 6; i++)
            if (canMove(diceToPiece[i].getColoredPiece(playerToMove), dummy))
                validRolls.add(i + 1); //valid dice rolls are in range 1-6, and i starts at 0

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
        if (action.type == PROMOTE || action.piece.canPromote(action.destination)) {
            next.setPiece(QUEEN.getColoredPiece(playerToMove), action.destination);
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
        PriorityQueue<Action> validActions = new PriorityQueue<>(new ActionComparator());
        Piece piece = diceToPiece[diceRoll - 1].getColoredPiece(playerToMove);
        Piece[] b = board.getBoard();

        for (int i = 0; i < b.length; i++) {
            //System.out.println("CONSIDERING BOARD SQUARE i: " + i);
            Piece p = b[i];
            Square location = Square.getSquareByIndex(i);
            if (location != Square.INVALID) {
                //System.out.println("CONSIDERING VALID SQUARE i: " + i);
                if (p == piece) {
                    //System.out.println("PIECE = " + p + " at: " + i);
                    switch (piece.getType()) {
                        //add score parameter to actions in order to put them in priority queue
                        //e.g. captures*value, protections, forwards, backwards, quiet
                        case KNIGHT, KING -> {
                            for (int offset : piece.getOffsets()) {
                                Square target = Square.getSquare(location.getSquareNumber() + offset);
                                if (target != Square.INVALID) {
                                    if (board.isEmpty(target)) {
                                        int manhattanDistance = Square.manhattanDistance(location, evilKing);
                                        int score = -3 * manhattanDistance + 60;
                                        if (p == KING)
                                            score = (manhattanDistance > 6) ? manhattanDistance : -manhattanDistance; //Shy king heuristic
                                        validActions.add(new Action(p, location, target, score, QUIET)); //TODO: manhattan distance
                                    } else if (!board.getPieceAt(target).isFriendly(playerToMove)) {
                                        Action.ActionType type = board.getPieceAt(target).getType() == KING ? WIN : CAPTURE;
                                        validActions.add(new Action(p, location, target, board.getPieceAt(target).getWeight(), type)); //capture
                                    }
                                }
                            }
                        }

                        case BISHOP, ROOK, QUEEN -> {
                            for (int offset : piece.getOffsets()) {
                                Square target = Square.getSquare(location.getSquareNumber() + offset);
                                while (target != INVALID) {
                                    if (board.isEmpty(target)) {
                                        validActions.add(new Action(p, location, target, -3 * Square.manhattanDistance(location, evilKing) + 60, QUIET)); //TODO manhattan distance
                                        target = Square.getSquare(target.getSquareNumber() + offset);
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
                            Square naturalMove = Square.getSquare(location.getSquareNumber() + piece.getOffsets()[0]);
                            if (naturalMove != Square.INVALID && board.isEmpty(naturalMove)) {
//                                if (!piece.canDoubleJump(location)) {
//                                    Action a = new Action(p, location, naturalMove, 16);
//                                    validActions.add(a);
//                                }

                                //double jumping
                                Square doubleJump = Square.getSquare(naturalMove.getSquareNumber() + piece.getOffsets()[0]);
                                if (doubleJump != Square.INVALID && board.isEmpty(doubleJump) && piece.canDoubleJump(location)) {
                                    validActions.add(new Action(p, location, doubleJump, -3 * Square.manhattanDistance(location, evilKing) + 60, QUIET));
                                } else {
                                    int score = -3 * Square.manhattanDistance(location, evilKing) + 60;
                                    Action.ActionType type = QUIET;
                                    if (p.canPromote(naturalMove)) {
                                        score = QUEEN.getWeight() - 100;
                                        type = PROMOTE;
                                    } else if (p.promotable(naturalMove) && doubleJump != INVALID && board.isEmpty(doubleJump))
                                        score = QUEEN.getWeight() - 200;
                                    validActions.add(new Action(p, location, naturalMove, score, type));
                                }
                            }

                            //now consider pawn capture moves which put the pawn in a different file
                            for (int k = 1; k < 3; k++) {
                                //loop through the two potential capture targets
                                Square captureTarget = Square.getSquare(location.getSquareNumber() + piece.getOffsets()[k]);
                                if (captureTarget != INVALID) {
                                    Piece atTarget = board.getPieceAt(captureTarget);
                                    if (atTarget != EMPTY && !atTarget.isFriendly(playerToMove)) {
                                        Action.ActionType type = atTarget.getType() == KING ? WIN : CAPTURE;
                                        int score = (p.canPromote(captureTarget)) ? atTarget.getWeight() + QUEEN.getWeight() : atTarget.getWeight();
                                        validActions.add(new Action(p, location, captureTarget, score, type)); //capture
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //System.out.println(validActions);
        //Collections.sort(validActions);
        //System.out.println("TOP THREE: " + validActions.get(0) + " and " + validActions.get(1) + " and " + validActions.get(2));
        return validActions;
    }

    //optimizable with piece list / concurrent traversal of board
    public boolean isTerminal() {
        //System.out.println("CHECKING TERMINAL...");
        boolean whiteKing = false, blackKing = false;
        for (int i = 0; i < board.getBoard().length; i++) {
            if (board.getBoard()[i] == Piece.WHITE_KING) {
                whiteKing = true;
                this.whiteKing = Square.getBoardIndexMap().get(i);
            } else if (board.getBoard()[i] == BLACK_KING) {
                blackKing = true;
                this.blackKing = Square.getBoardIndexMap().get(i);
            }

            if (whiteKing && blackKing) { //both kings on the board, so not terminal
                //System.out.println("TERMINAL FALSE");
                return false;
            }
        }
        //if playerToMove == WHITE and !whiteKing -> LOSE -> Winner = BLACK
        //if playerToMove == BLACK and !blackKing -> LOSE -> Winner = WHITE
        //if playerToMove == WHITE and blackKing -> LOSE -> Winner = BLACK
        //if playerToMove == WHITE and !blackKing -> WIN -> WINNER = WHITE
        //if playerToMove == BLACK and white
        if (playerToMove == WHITE && !whiteKing) {
            //if WHITE is to move, but there is no white King, then black captured the white king
            winner = BLACK;
//            System.out.println("BLACK WINS");
//            board.printBoard();
        } else if (playerToMove == BLACK && !blackKing) {
            winner = WHITE;
//            System.out.println("WHITE WINS");
//            board.printBoard();
        } else {
            System.out.println("NEITHER WHITE NOR BLACK WON THE GAME?");
        }
        //System.out.println("TERMINAL TRUE");
        return true; //either one of the kings is not on the board
    }

    Side winner = null;

    public double reward(Side player) {
        if (terminal) {
            return player == playerToMove ? 0 : 1.0; //0 reward if opponent's move created this terminal state
        }
        return 0.5; //in non-terminal states, we may want to terminate simulation phase after x turns and declare draw
    }

    public static void main(String[] args) {
        TreeState ts = new TreeState(new Board0x88(Config.OPENING_FEN), WHITE, 0);
        //System.out.println(Arrays.toString(ts.board.getBoard()));
        System.out.println("Input array: " + Arrays.toString(ts.normalizedBoardArray()));
    }

    public int[] normalizedBoardArray() {
        int[] b = new int[64 + 6]; //all boards + all piece types
        int index = 0;
        //int i = 0;
        Piece[] x88 = board.getBoard();

        playerToMove = BLACK;

        for (int i = 0; i < x88.length; i++) {
            if (x88[i] == OFF_BOARD) {
                i += 7;
                System.out.println();
            } else {
                b[index++] = x88[i].getPiecePerspective(playerToMove);
                System.out.print(x88[i] + ", ");
            }
        }

        int roll = 5;
        b[index + roll - 1] = 1;

        return b;
    }
}
