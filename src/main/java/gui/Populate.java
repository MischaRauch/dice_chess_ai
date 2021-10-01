package gui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.image.Image;


public class Populate { //CURRENTLY NOT BEING USED BECAUSE I COULDN'T GET IT TO WORK

    public static final int initWCOL = 8;
    public static final int initWROW = 2;

    PieceGui whiteRook = new PieceGui(new ImageView(new Image("/images/pieces/w_rook.png")), "W");
    PieceGui whiteKnight = new PieceGui(new ImageView(new Image("/images/pieces/w_knight.png")), "W");
    PieceGui whiteBishop = new PieceGui(new ImageView(new Image("/images/pieces/w_bishop.png")), "W");
    PieceGui whiteQueen = new PieceGui(new ImageView(new Image("/images/pieces/w_queen.png")), "W");
    PieceGui whiteKing = new PieceGui(new ImageView(new Image("/images/pieces/w_king.png")), "W");
    PieceGui whitePawn = new PieceGui(new ImageView(new Image("/images/pieces/w_pawn.png")), "W");


    public Tile populatePieces(int col, int row){
        VBox v = new VBox();
        if(row == 1 && col==1)
            v.getChildren().add(whiteRook.getPieceView());
        else
            v.getChildren().add(new Label("PIECE"));
        Tile t = new Tile(v,(row+col)%2== 0, true, row, col);

        return t;
    }

}
