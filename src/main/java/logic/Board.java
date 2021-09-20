package logic;


public class Board {

    enum Pieces {e, P, N, B, R, Q, K, p, n, b, r, q, k, o}

    public static void main(String[] args) {
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


        // loop over ranks
        for (int rank = 0; rank < 8; rank++) {
            // loop over files
            for (int file = 0; file < 16; file++) {
                //initialize square
                int tile = rank * 16 + file; // this converts to board index

                if((tile & 0x88) == 0)
                    System.out.print(board[tile].ordinal() + " "); //the same output
                //System.out.print(tile + " ");
            }
            System.out.print("\n");
        }

    }
}

