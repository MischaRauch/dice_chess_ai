package gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoadChessImages {

    char[] charOfPieces = {'p','P','r','R','n','N','b','B','q','Q','k','K'};
    String[] imgOfChars = {"images/pieces/b_pawn.png", "images/pieces/w_pawn.png", "images/pieces/b_rook.png", "images/pieces/w_rook.png",
            "images/pieces/b_knight.png", "images/pieces/w_knight.png", "images/pieces/b_bishop.png", "images/pieces/w_bishop.png",
            "images/pieces/b_queen.png", "images/pieces/w_queen.png", "images/pieces/b_king.png", "images/pieces/w_king.png"};

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


