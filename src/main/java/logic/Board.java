package logic;


import java.util.Arrays;

public class Board {

    enum Pieces {e, P, N, B, R, Q, K, p, n, b, r, q, k, o}
    static int[] mainBoard = new int[128];

    public static void main(String args[]) {

        int [] tempBoard = new int[128];
        tempBoard = createBoard();
        System.out.print(Arrays.toString(getBoard()));

    }

    public static int[] createBoard() {

        //initalize pieces
        Pieces e = Pieces.e; Pieces P = Pieces.P; Pieces N = Pieces.N; Pieces B = Pieces.B; Pieces R = Pieces.R;
        Pieces Q = Pieces.Q; Pieces K = Pieces.K; Pieces p = Pieces.p; Pieces n = Pieces.n; Pieces b = Pieces.b;
        Pieces r = Pieces.r; Pieces q = Pieces.q; Pieces k = Pieces.k; Pieces o = Pieces.o;
        //set up board
        Pieces[] board = {
                r, n, b, q, k, b, n, r,  o, o, o, o, o, o, o, o,
                p, p, p, p, p, p, p, p,  o, o, o, o, o, o, o, o,
                e, e, e, e, e, e, e, e,  o, o, o, o, o, o, o, o,
                e, e, e, e, e, e, e, e,  o, o, o, o, o, o, o, o,
                e, e, e, e, e, e, e, e,  o, o, o, o, o, o, o, o,
                e, e, e, e, e, e, e, e,  o, o, o, o, o, o, o, o,
                P, P, P, P, P, P, P, P,  o, o, o, o, o, o, o, o,
                R, N, B, Q, K, B, N, R,  o, o, o, o, o, o, o, o,
        };

        // a file = first column, b file = second column etc
        //    String ascii_pieces = ".PNBRQKpnbrqko";
        //    char[] ascii = {'.', 'P', 'N', 'B', 'R', 'Q', 'K', 'p', 'b', 'r', 'q', 'k', 'o'};
        //    char[] char_ascii_pieces = ascii_pieces.toCharArray();

        int temp = 0;

        // loop over ranks
        for (int ranks = 0; ranks < 8; ranks++) {
            // loop over files
            for (int files = 0; files < 16; files++) {
                //initialize square
                int tile = ranks * 16 + files; // this converts to board index
                if((tile & 0x88) == 0) {
                    mainBoard[temp] = board[tile].ordinal();
                    temp++;
                }
                //System.out.print(board[tile].ordinal() + " "); //the same output
                //System.out.print(tile + " ");
            }
            // System.out.print("\n");
        }
        return mainBoard;
    }

    public static int[] getBoard() {
        return mainBoard;
    }
}

