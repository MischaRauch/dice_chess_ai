package gui;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import logic.enums.Piece;
import logic.enums.Square;

//I threw this class together real quick to test demonstrate that you can also make custom JavaFX components in pure Java
//and still be able to add them to Scenes or as children of other Nodes (regardless if create in FXML or not)
//each Tile is a Vbox bc I like Vboxes and they have the alignment property so you can center its children, which is nicer
//than just using a Pane. (Also I'm pretty sure Vbox extends from Pane so Vbox's are really just fancier Panes)
public class Tile extends VBox {

    public static Tile selectedTile = null;
    private final Square square; //also useful for parsing move
    private final int row, col;
    //useful for parsing the Move, and also to check if a Tile is empty, since you can check if piece == Piece.EMPTY
    private Piece piece;
    private ImageView view = new ImageView();

    public Tile(char p, int row, int col) {
        this.row = row;
        this.col = col;

        if ((row + col) % 2 == 0) {
            //white cells: row + col % 2 == 0
            setStyle("-fx-background-color: #d5a47d");
        } else {
            //black cells: row + col % 2 == 1
            setStyle("-fx-background-color: #98501a");
        }

        if (Character.isLetter(p))
            view = ChessIcons.load(p);

        piece = Piece.getPieceFromChar(p);
        square = Square.getSquare(7 - row, col);

        view.setFitHeight(80);
        view.setPreserveRatio(true);

        getChildren().add(view);
        setAlignment(Pos.CENTER);
    }

    private void updateView() {
        getChildren().removeAll(getChildren());
        view.setFitHeight(80);
        view.setPreserveRatio(true);
        getChildren().add(view);
    }

    public void select() {
        selectedTile = this;
        setStyle("-fx-background-color: #2ecc71");
    }

    public void unselect() {
        selectedTile = null;
    }

    public void colorGreen() {
        setStyle("-fx-background-color: #2ecc71");
//        updateView(); //may be
    }

    public void colorDefault() {
        if ((row + col) % 2 == 0) {
            //white cells: row + col % 2 == 0
            setStyle("-fx-background-color: #d5a47d");
        } else {
            //black cells: row + col % 2 == 1
            setStyle("-fx-background-color: #98501a");
        }
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        view = piece != Piece.EMPTY ? ChessIcons.load(piece) : new ImageView();
        updateView();
    }

    public Square getSquare() {
        return square;
    }

    public ImageView getView() {
        return view;
    }

    //0-indexed
    public int getRow() {
        return row;
    }

    //0-indexed
    public int getCol() {
        return col;
    }
}
