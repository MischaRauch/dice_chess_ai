package logic.expectiminimax;

import logic.enums.Piece;
import logic.game.Game;

import java.util.ArrayList;
import java.util.List;

import static logic.enums.Piece.*;
import static logic.enums.Piece.BLACK_ROOK;

public class Node {

    private int boardEvaluationNumber;
    private Piece[][] boardPieceState = {
                    {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_QUEEN,WHITE_KING,WHITE_BISHOP,WHITE_KNIGHT,WHITE_ROOK},
                    {WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,},
                    {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY},
                    {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY},
                    {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY},
                    {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY},
                    {BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN},
                    {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_QUEEN,BLACK_KING,BLACK_BISHOP,BLACK_KNIGHT,BLACK_ROOK}};
    private boolean isMaxPlayer;
    private int score;
    private List<Node> children;

    public Node(int boardEvaluationNumber, boolean isMaxPlayer) {
        //this.boardPieceState = boardPieceState;
        this.isMaxPlayer = isMaxPlayer;
        this.boardEvaluationNumber = boardEvaluationNumber;
        children = new ArrayList<>();
                //.getBoardEvaluationNumber();
    }

    public int getBoardEvaluationNumber() {
        return boardEvaluationNumber;
        //return BoardStateEvaluator.getBoardEvaluationNumber(boardPieceState);
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


}
