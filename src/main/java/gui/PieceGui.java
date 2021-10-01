package gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class PieceGui {

    //imageview for the piece
    //instructions for the piece

    private ImageView pieceView;
    //private Label pieceInstruction;
    private String pieceID;

    public PieceGui(ImageView pieceView, String pieceID) {
        this.pieceView = pieceView;
        this.pieceID = pieceID;
    }

    public ImageView getPieceView() {
        return pieceView;
    }

    public void setPieceView(ImageView pieceView) {
        this.pieceView = pieceView;
    }



    /*public Label getPieceInstruction() {
        return pieceInstruction;
    }

    public void setPieceInstruction(Label pieceInstruction) {
        this.pieceInstruction = pieceInstruction;
    }
     */
}
