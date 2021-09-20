package gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoadChessImages {

    char[] charOfPieces = {'p','P','r','R','n','N','b','B','q','Q','k','K'};
    String[] imgOfChars = {"pieces/b_pawn.png", "pieces/w_pawn.png", "pieces/b_rook.png", "pieces/w_rook.png",
            "pieces/b_knight.png", "pieces/w_knight.png", "pieces/b_bishop.png", "pieces/w_bishop.png",
            "pieces/b_queen.png", "pieces/w_queen.png", "pieces/b_king.png", "pieces/w_king.png" };

    char[] whichChar = {'p', 'n', 'b', 'r', 'q', 'k'};

    public ImageView loadImage(char p) {
        ImageView view;

        for (int i=0; i< charOfPieces.length; i++) {
            if (p == charOfPieces[i]) {
                Image img = new Image(imgOfChars[i]);
                view = new ImageView(img);
                view.setFitHeight(80);
                view.setPreserveRatio(true);
                return view;
            }
        }
        return null;
    }

    public char whichPiece(int diceRoll, boolean whiteTurn) {
        for (int i=0; i < whichChar.length; i++) {
            if (diceRoll == i) {
                if (whiteTurn) {
                    return Character.toUpperCase(whichChar[i]);
                }
                return whichChar[i];
            }
        }
        return ' ';
    }
}


