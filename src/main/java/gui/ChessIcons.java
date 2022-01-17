package gui;

import javafx.scene.image.ImageView;
import logic.enums.Piece;
import logic.enums.Side;

import java.util.EnumMap;

import static logic.enums.Piece.*;

public class ChessIcons {

    private static final char[] diceToPiece = {'p', 'n', 'b', 'r', 'q', 'k'};

    //EnumMaps are like normal Maps, except that they are fancier and faster and special and cool, read more online
    //the EnumMap constructor needs to know which enum it is mapping, so that's why Piece.class is in the constructor
    private static final EnumMap<Piece, String> imgMap = new EnumMap<>(Piece.class);

    static {
        //the code in this block gets run (only once) the first time the ChessIcons class is referenced in our application code
        //you can think of it like a constructor for static fields
        //I use it to populate the EnumMap with the values
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

    /**
     * This static load method references the EnumMap to instantly get the image url associated with that piece
     * and creates an ImageView out of it
     *
     * @param piece The Piece enum which to load the ImageView of
     * @return ImageView
     */
    public static ImageView load(Piece piece) {
        return new ImageView(imgMap.get(piece));
    }

    /**
     * This static load method converts a char to a Piece, and then calls the above load method to return the
     * associated ImageView
     *
     * @param p char representing which piece. Case sensitive since case determines color
     * @return ImageView associated with the Piece associated with the char p
     */
    public static ImageView load(char p) {
        return load(Piece.getPieceFromChar(p));
    }

    /**
     * Another static load method which should display load an ImageView associated with the dice roll and logic.player whose
     * turn it is. It uses the static diceToPiece char array to get the char for the roll number, and then converts that
     * char to upper case if the Side == WHITE. Then it uses the above load method with the char in order to return the
     * relevant ImageView
     *
     * @param diceRoll integer in range of 1-6
     * @param side     Enum Side of the logic.player who rolled
     * @return ImageView associated with the piece of the roll number with the color of the logic.player who rolled.
     */
    public static ImageView load(int diceRoll, Side side) {
        //syntax is a little weird here bc I wanted to one-line all the methods in this class, but if you are not familiar
        //with this syntax, it's basically an if/else statement in one line.
        //read it like: (if) side equals Side.WHITE (then) load using the uppercase piece, (else) load using the default lowercase piece char
        return side == Side.WHITE ? load(Character.toUpperCase(diceToPiece[diceRoll - 1])) : load(diceToPiece[diceRoll - 1]);
    }
}
