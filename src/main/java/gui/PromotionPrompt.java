package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import logic.enums.Piece;
import logic.enums.Side;

import java.io.IOException;

import static logic.enums.Piece.*;
import static logic.enums.Side.WHITE;

public class PromotionPrompt extends AnchorPane {

    private Side color;

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

    PromotionPrompt key = this;

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
