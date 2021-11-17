package logic.expectiminimax;

public class Node {

    private int value;
    private Node PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;

//    public Node (int n, Node PAWN, Node KNIGHT, Node BISHOP, Node ROOK, Node QUEEN, Node KING) {
//        value = n;
//        PAWN = null; KNIGHT = null; BISHOP = null; ROOK = null; QUEEN = null; KING = null;
//    }

    public Node(int n) {
        Node temp = new Node(n);
        temp.value = n;
        temp.PAWN = null; temp.KNIGHT = null; temp.BISHOP = null; temp.ROOK = null; temp.QUEEN = null; temp.KING = null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getPAWN() {
        return PAWN;
    }

    public void setPAWN(Node PAWN) {
        this.PAWN = PAWN;
    }

    public Node getKNIGHT() {
        return KNIGHT;
    }

    public void setKNIGHT(Node KNIGHT) {
        this.KNIGHT = KNIGHT;
    }

    public Node getBISHOP() {
        return BISHOP;
    }

    public void setBISHOP(Node BISHOP) {
        this.BISHOP = BISHOP;
    }

    public Node getROOK() {
        return ROOK;
    }

    public void setROOK(Node ROOK) {
        this.ROOK = ROOK;
    }

    public Node getQUEEN() {
        return QUEEN;
    }

    public void setQUEEN(Node QUEEN) {
        this.QUEEN = QUEEN;
    }

    public Node getKING() {
        return KING;
    }

    public void setKING(Node KING) {
        this.KING = KING;
    }
}
