package gui.controllers;

import logic.enums.Piece;
import logic.enums.Side;
import gui.ChessIcons;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import static logic.enums.Piece.*;
import static logic.enums.Side.WHITE;


public class PromotionPrompt extends AnchorPane {

    private final Side color;

    private final PromotionPrompt key = this;

    public PromotionPrompt(Side color) {
        this.color = color;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/promotionPane.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();;
        }

    }

    @FXML
    void initialize() {
        if (color == WHITE) {
            availablePieces.getChildren().add(createOption(WHITE_KNIGHT));
            availablePieces.getChildren().add(createOption(WHITE_BISHOP));
            availablePieces.getChildren().add(createOption(WHITE_ROOK));
            availablePieces.getChildren().add(createOption(WHITE_QUEEN));
        } else {
            availablePieces.getChildren().add(createOption(BLACK_KNIGHT));
            availablePieces.getChildren().add(createOption(BLACK_BISHOP));
            availablePieces.getChildren().add(createOption(BLACK_ROOK));
            availablePieces.getChildren().add(createOption(BLACK_QUEEN));
        }

        MainContainerController.modal.getChildren().add(this);
        MainContainerController.modal.setVisible(true);
    }

    private ImageView createOption(Piece piece) {
        ImageView icon = ChessIcons.load(piece);
        icon.setOnMouseClicked(event -> {
            MainContainerController.modal.getChildren().remove(key);
            MainContainerController.modal.setVisible(false);
            Platform.exitNestedEventLoop(key, piece);

        });

        icon.setFitWidth(80);
        icon.setPreserveRatio(true);
        icon.setCursor(Cursor.HAND);
        icon.setOpacity(0.7);


        icon.setOnMouseEntered(event -> icon.setOpacity(1.0));
        icon.setOnMouseExited(event -> icon.setOpacity(0.7));

        return icon;
    }

    @FXML
    private HBox availablePieces;

}
