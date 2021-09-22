package logic;

public class State {

    Board board;
    // castleing rights
    // en passant square
    // player turn
    //

    public State(Board board) {
        this.board = board;
    }

    public State(String FEN) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public void loadFEDandUpdateBoard(String FEN) {
        // update current board using FEN state
    }

    public void updateBoard(char piece,int startPos, int endPos) {
        //
    }

    public int getPiecePos(char piece) {
        int pos = 0;
        return pos;
    }


    public String returnFEN() {
        return "FEN here";
    }


}


