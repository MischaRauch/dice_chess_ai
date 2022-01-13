package logic.mcts;

import logic.enums.Piece;
import logic.enums.Square;

public class Action implements Comparable<Action> {
    enum ActionType {WIN, CAPTURE, QUIET}

    Piece piece;
    Square origin;
    Square destination;
    double score;
    ActionType type;

    //maybe store type (e.g. promotion, capture, check, quiet)
    //maybe store heuristic value, maybe to use as move ordering to consider
    //store player?

    public Action(Piece p, Square o, Square d, double s, ActionType type) {
        piece = p;
        origin = o;
        destination = d;
        score = s;
        this.type = type;
    }


    @Override
    public int compareTo(Action o) {
        if (this.score == o.score) return 0;
        return this.score > o.score ? (int) score : -1;
    }

    @Override
    public String toString() {
        return "Action(" + origin.toString() + ", " + piece.getUnicode() + ", " + destination.toString() + ", " + type + ", score: " + score + ")";
    }

}
