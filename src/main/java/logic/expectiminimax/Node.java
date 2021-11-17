package logic.expectiminimax;

import logic.enums.Piece;
import logic.game.Game;

import java.util.List;

public class Node {

    private int boardEvaluationNumber;
    private Piece[][] boardPieceState;
    private boolean isMaxPlayer;
    private int score;
    private List<Node> children;

    public Node(int boardEvaluationNumber, boolean isMaxPlayer) {
        //this.boardPieceState = boardPieceState;
        this.isMaxPlayer = isMaxPlayer;
        this.boardEvaluationNumber = boardEvaluationNumber;
                //.getBoardEvaluationNumber();
    }

    public int getBoardEvaluationNumber() {
        return BoardStateEvaluator.getBoardEvaluationNumber();
    }

    public Piece[][] getBoardPieceState() {
        return boardPieceState;
    }

    public void setBoardPieceState(Piece[][] boardPieceState) {
        this.boardPieceState = boardPieceState;
    }

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


//    public Node (int n, Node PAWN, Node KNIGHT, Node BISHOP, Node ROOK, Node QUEEN, Node KING) {
//        value = n;
//        PAWN = null; KNIGHT = null; BISHOP = null; ROOK = null; QUEEN = null; KING = null;
//    }

}
