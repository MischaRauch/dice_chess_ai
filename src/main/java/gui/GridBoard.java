package gui;


import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;

import java.net.URL;
import java.util.ResourceBundle;

public class GridBoard  {

    public static final int TILE_SIZE = 80;
    public static final int ROWS = 8;
    public static final int COLS = 8;


    public Parent create(){

        GridPane grid = new GridPane();
        grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        //add tiles
        for(int r = 0; r < ROWS; r++){
            for(int c = 0; c < COLS; c++){
                grid.getChildren().add(getTile(r, c));
            }
        }

        //Construct column constraints to resize horizontaly
        ObservableList<ColumnConstraints> colCconstraints = grid.getColumnConstraints();
        for(int col = 0; col < COLS; col++){
            ColumnConstraints c = new ColumnConstraints();
            c.setHalignment(HPos.CENTER);
            c.setHgrow(Priority.ALWAYS);
            colCconstraints.add(c);
        }

        //Construct row constraints to resize vertically
        ObservableList<RowConstraints> rowCconstraints = grid.getRowConstraints();
        for(int row = 0; row < ROWS; row++){
            RowConstraints c = new RowConstraints();
            c.setValignment(VPos.CENTER);
            c.setVgrow(Priority.ALWAYS);
            rowCconstraints.add(c);
        }


        return grid;
    }

    public Tile getTile(int r, int c){

        Tile t = new Tile(new VBox(),(r+c)%2== 0, true, r, c);
        //Populate pop = new Populate();
        //Tile t = pop.populatePieces(r,c);

        t.setPrefHeight(80);
        t.setPrefWidth(80);
        GridPane.setRowIndex(t, r);
        GridPane.setColumnIndex(t, c);

        return t;
    }


}
