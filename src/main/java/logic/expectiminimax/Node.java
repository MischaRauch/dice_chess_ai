package logic.expectiminimax;

import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import logic.game.Game;

import java.util.*;

import static logic.enums.Piece.*;
import static logic.enums.Piece.BLACK_ROOK;

public class Node {

    private boolean isMaxPlayer;
    private int score;
    private List<Node> children;
    private Side color;
    private int diceRoll;
    private int boardEvaluationNumber;
    private State state;

    public Node(boolean isMaxPlayer, Side color, int diceRoll, int boardEvaluationNumber, State state) {
        this.isMaxPlayer = isMaxPlayer;
        this.color = color;
        this.diceRoll = diceRoll;
        this.boardEvaluationNumber = boardEvaluationNumber;
        this.state = state;
        this.children = new ArrayList<>();
    }

    // for root
    public Node(Side color, int diceRoll, boolean isMaxPlayer, State state) {
        this.color=color;
        this.diceRoll=diceRoll;
        this.isMaxPlayer=isMaxPlayer;
        this.state = state;
        this.children = new ArrayList<>();
    }


//    public Piece[][] getBoardPieceState() {
//        return boardPieceState;
//    }

    public int getBoardEvaluationNumber() {
        return boardEvaluationNumber;
    }

    //    public void setEvaluationNumberAndBoardStateMappings(Map<Integer, Piece[][]> evaluationNumberAndBoardStateMappings) {
//        this.evaluationNumberAndBoardStateMappings = evaluationNumberAndBoardStateMappings;
//    }



//    public Node(List<Integer> evaluationNumbers, List<Piece[][]> boardPieceStates, Side color, int diceRoll, boolean isMaxPlayer) {
//        this.isMaxPlayer = isMaxPlayer;
//        this.color=color;
//        this.diceRoll=diceRoll;
//        this.evaluationNumbers=evaluationNumbers;
//        this.boardPieceStates=boardPieceStates;
//        children = new ArrayList<>();
//        // tree map auto sorts
//        evaluationNumberAndBoardStateMappings = new TreeMap<>();
//        // add states to map
//        for (int i = 0; i < evaluationNumbers.size(); i++) {
//            evaluationNumberAndBoardStateMappings.put(evaluationNumbers.get(i),boardPieceStates.get(i));
//        }
//        // sort accending
//        // evaluationNumberAndBoardStateMappings = new TreeMap<Integer, Piece[][]>(evaluationNumberAndBoardStateMappings);
//        // sort decending, max first
//        evaluationNumberAndBoardStateMappings = new TreeMap<Integer, Piece[][]>(Collections.reverseOrder());
//    }


    public void addChild(Node child) {
        children.add(child);
    }

    public boolean isMaxPlayer() {
        return isMaxPlayer;
    }

    public void setMaxPlayer(boolean maxPlayer) {
        isMaxPlayer = maxPlayer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Side getColor() {
        return color;
    }

    public void setColor(Side color) {
        this.color = color;
    }

    public int getDiceRoll() {
        return diceRoll;
    }

    public void setDiceRoll(int diceRoll) {
        this.diceRoll = diceRoll;
    }

    public State getState() {
        return state;
    }
}
