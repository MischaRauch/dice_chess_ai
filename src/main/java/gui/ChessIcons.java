package gui;

import javafx.scene.image.ImageView;
import logic.enums.Piece;
import logic.enums.Side;

import java.util.EnumMap;

import static logic.enums.Piece.*;

public class ChessIcons {

    private static final char[] diceToPiece = {' ', 'p', 'n', 'b', 'r', 'q', 'k'};
    private static final EnumMap<Piece, String> imgMap = new EnumMap<>(Piece.class);
    static {
        imgMap.put(WHITE_PAWN, "/images/pieces/w_pawn.png");
        imgMap.put(WHITE_KNIGHT, "/images/pieces/w_knight.png");
        imgMap.put(WHITE_BISHOP, "/images/pieces/w_bishop.png");
        imgMap.put(WHITE_ROOK, "/images/pieces/w_rook.png");
        imgMap.put(WHITE_QUEEN, "/images/pieces/w_queen.png");
        imgMap.put(WHITE_KING, "/images/pieces/w_king.png");

        imgMap.put(BLACK_PAWN, "/images/pieces/b_pawn.png");
        imgMap.put(BLACK_KNIGHT, "/images/pieces/b_knight.png");
        imgMap.put(BLACK_BISHOP, "/images/pieces/b_bishop.png");
        imgMap.put(BLACK_ROOK, "/images/pieces/b_rook.png");
        imgMap.put(BLACK_QUEEN, "/images/pieces/b_queen.png");
        imgMap.put(BLACK_KING, "/images/pieces/b_king.png");
    }

    public static ImageView load(Piece piece) {
        return new ImageView(imgMap.get(piece));
    }

    public static ImageView load(char p) {
        return load(Piece.getPieceFromChar(p));
    }

    public static ImageView load(int diceRoll, Side side) {
        return side == Side.WHITE ? load(Character.toUpperCase(diceToPiece[diceRoll])) : load(diceToPiece[diceRoll]);
    }
}
