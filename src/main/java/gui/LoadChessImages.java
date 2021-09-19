package gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;


public class LoadChessImages {
    public ImageView loadImage(char p) {
        ImageView view = new ImageView();
        if (p == 'p') {
            Image img = new Image("pieces/b_pawn.png");
            view = new ImageView(img);
        }
        else if ( p == 'P') {
            Image img = new Image("pieces/w_pawn.png");
            view = new ImageView(img);
        }
        else if ( p == 'r') {
            Image img = new Image("pieces/b_rook.png");
            view = new ImageView(img);
        }
        else if ( p == 'R') {
            Image img = new Image("pieces/w_rook.png");
            view = new ImageView(img);
        }
        else if ( p == 'n') {
            Image img = new Image("pieces/b_knight.png");
            view = new ImageView(img);
        }
        else if ( p == 'N') {
            Image img = new Image("pieces/w_knight.png");
            view = new ImageView(img);
        }
        else if ( p == 'b') {
            Image img = new Image("pieces/b_bishop.png");
            view = new ImageView(img);
        }
        else if ( p == 'B') {
            Image img = new Image("pieces/w_bishop.png");
            view = new ImageView(img);
        }
        else if ( p == 'q') {
            Image img = new Image("pieces/b_queen.png");
            view = new ImageView(img);
        }
        else if ( p == 'Q') {
            Image img = new Image("pieces/w_queen.png");
            view = new ImageView(img);
        }
        else if ( p == 'k') {
            Image img = new Image("pieces/b_king.png");
            view = new ImageView(img);
        }
        else if ( p == 'K') {
            Image img = new Image("pieces/w_king.png");
            view = new ImageView(img);
        }

        else {
            return view;
        }
        view.setFitHeight(80);
        view.setPreserveRatio(true);
        return view;
    }
}
