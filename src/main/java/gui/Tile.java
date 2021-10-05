package gui;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import logic.enums.Piece;
import logic.enums.Square;

public class Tile extends VBox {

    private Piece piece;
    private final Square square;

    private ImageView view = new ImageView();

    private final int row, col;

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
        square = Square.getSquare(row, col);

        view.setFitHeight(80);
        view.setPreserveRatio(true);

        getChildren().add(view);
        setAlignment(Pos.CENTER);
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        view = ChessIcons.load(piece);
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
