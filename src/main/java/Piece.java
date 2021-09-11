import java.util.LinkedList;

public class Piece {
    int x;
    int y;
    boolean isBlack;
    LinkedList<Piece> state;
    String name;
    public Piece(int x, int y, boolean isBlack, String name, LinkedList<Piece> state) {
        this.x=x;
        this.y=y;
        this.isBlack=isBlack;
        this.name=name;
        state.add(this);
    }
    public void move(int x, int y) {
        //state.stream().filter((p) -> (p.x==x&&.y==y)).forEachOrdered((p) -> (p.delete()));
        this.x=x;
        this.y=y;
    }
    public void delete() {
        state.remove(this);
    }

}
