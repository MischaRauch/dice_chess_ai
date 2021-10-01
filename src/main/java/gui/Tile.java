package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Tile extends Pane {


    //PIECE
    //BACKGROUND
    //EMPTY

    //private PieceGui pieceGui;
    private VBox recBox;
    private boolean isEmpty;
    private boolean fillBool;
    private int row;
    private int col;

    public Tile(VBox recBox, boolean fillBool, boolean isEmpty, int row, int col) {
        this.recBox = recBox;
        this.isEmpty = isEmpty;
        this.fillBool = fillBool;
        this.row = row;
        this.col = col;

        //recBox.getChildren().add(new ImageView(new Image("/images/pieces/w_knight.png")));

        if(!fillBool)
            setBackground(new Background(new BackgroundFill(Color.valueOf("#588c7e"), CornerRadii.EMPTY, Insets.EMPTY)));
        else
            setBackground(new Background(new BackgroundFill(Color.valueOf("#ffeead"), CornerRadii.EMPTY, Insets.EMPTY)));


    }

    public VBox getRecBox() {
        return recBox;
    }

    public void setRecBox(VBox recBox) {
        this.recBox = recBox;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public boolean isFillBool() {
        return fillBool;
    }

    public void setFillBool(boolean fillBool) {
        this.fillBool = fillBool;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

}
