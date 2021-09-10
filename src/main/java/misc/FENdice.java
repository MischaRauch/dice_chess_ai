package misc;

import java.util.HashMap;

public class FENdice {
                    //0     1     2    3    5    5
    String[] wPiece = {"♙", "♘", "♗", "♖","♕","♔"};
    String[] bPiece = {"♟", "♞", "▰", "♜","♛","♚"};

    HashMap<Character, String> pieceMap = new HashMap<>();

    String fenDiceBoard = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1"; //last number is dice roll

    public FENdice() {
        pieceMap.put('p', "♟");
        pieceMap.put('n', "♞");
        pieceMap.put('b', "▰");
        pieceMap.put('r', "♜");
        pieceMap.put('q', "♛");
        pieceMap.put('k', "♚");

        pieceMap.put('P', "♙");
        pieceMap.put('N', "♘");
        pieceMap.put('B', "♗");
        pieceMap.put('R', "♖");
        pieceMap.put('Q', "♕");
        pieceMap.put('K', "♔");
    }

    public void printBoard(String[][] board) {
        for (int i = 0; i < board.length; i++) {
            if (i % 2 == 0) {
                if (i!=0) {
                    System.out.println(" 　　＼＼＼　　　＼＼＼　　　＼＼＼　　　＼＼＼");
                    //　　　＃＃＃　　　＃＃＃　　　＃＃＃　　　＃＃＃　　　＃＃＃
                    System.out.print("　" + board[i][0] + "　");
                    for (int j = 1; j < board.length; j++) {
                        if (j % 2 == 0)
                            System.out.print("　" + board[i][j] + "　");
                        else
                            System.out.print("＼" + board[i][j] + "＼");
                    }
                    System.out.println("\n 　　＼＼＼　　　＼＼＼　　　＼＼＼　　　＼＼＼");
                } else {
                    System.out.println(" 　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
                    for (int j = 0; j < board.length; j++) {
                        System.out.print("　" + board[i][j] + "　");

                    }
                    System.out.println("\n 　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
                }
            } else {
                System.out.println(" 　　　　　＼＼＼　　　＼＼＼　　　＼＼＼　　　＼＼＼");
                //　　　＃＃＃　　　＃＃＃　　　＃＃＃　　　＃＃＃　　　＃＃＃
                System.out.print("　" + board[i][0] + "　");
                for (int j = 1; j < board.length; j++) {
                    if (j % 2 == 0)
                        System.out.print("＼" + board[i][j] + "＼");
                    else
                        System.out.print("　" + board[i][j] + "　");
                }
                System.out.println("\n 　　　　　＼＼＼　　　＼＼＼　　　＼＼＼　　　＼＼＼");
            }
        }

    }

    public String[][] clearBoard() {
        return new String[][]{
            {"　", "A", "B", " C", " D", "E", " F", " G", "H"},
            {"8", "　", "　", "　", "　", "　", "　", "　", "　"},
            {"7", "　", "　", "　", "　", "　", "　", "　", "　"},
            {"6", "　", "　", "　", "　", "　", "　", "　", "　"},
            {"5", "　", "　", "　", "　", "　", "　", "　", "　"},
            {"4", "　", "　", "　", "　", "　", "　", "　", "　"},
            {"3", "　", "　", "　", "　", "　", "　", "　", "　"},
            {"2", "　", "　", "　", "　", "　", "　", "　", "　"},
            {"1", "　", "　", "　", "　", "　", "　", "　", "　"},

        };
    }

    public String[][] parseFENdice(String fenDiceBoard) {
        String[][] board = clearBoard();


        String[] info = fenDiceBoard.split("/|\\s");

        String activeColor = info[8];
        String castling = info[9];
        String enPassant = info[10];
        String halfmoveClock = info[11];
        String fullmoveNumber = info[12];
        int roll = Integer.parseInt(info[13]);

        for (int i = 0; i < 8; i++) {
            char[] rankSequence = info[i].toCharArray();
            String[] rank = board[i+1];
            int index = 1;
            for (char c : rankSequence) {
                if (Character.isDigit(c)) {
                    index += Character.getNumericValue(c);
                } else if (Character.isAlphabetic(c)) {
                    rank[index++] = pieceMap.get(c);
                }
            }
        }


        return  board;
    }

    public static void main(String[] args) {
        FENdice feNdice = new FENdice();
        feNdice.printBoard(feNdice.parseFENdice(feNdice.fenDiceBoard));
        System.out.println("");
        feNdice.printBoard(feNdice.parseFENdice("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1 1"));
        System.out.println("");
        feNdice.printBoard(feNdice.parseFENdice("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2 1"));
        System.out.println("");
        feNdice.printBoard(feNdice.parseFENdice("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2 5"));
    }
}
