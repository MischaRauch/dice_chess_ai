package logic;

import java.util.List;

public class BoardStateAndEvaluationNumberTuple<X,Y> {

    public final List<List<PieceAndSquareTuple>> x;
    public final List<Integer> y;

    public BoardStateAndEvaluationNumberTuple(List<List<PieceAndSquareTuple>> boardStates, List<Integer> evaluationNumbers) {
        this.x = boardStates;
        this.y = evaluationNumbers;
    }
    public List<List<PieceAndSquareTuple>> getBoardStates() {
        return x;
    }

    public List<Integer> getEvaluationNumbers() {
        return y;
    }
}
