package gui.controllers;

import javafx.scene.Node;
import javafx.stage.Modality;
import logic.enums.Side;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.player.*;

import java.io.IOException;

import static logic.enums.Side.WHITE;

public class GameOverScreen extends AnchorPane {

    Side winner;
    AIPlayer winnerPlayer;
    BasicAIPlayer basicAI;
    ExpectiMiniMaxPlayer expectiMiniMax;
    MiniMaxPlayer miniMaxPlayer;
    QLPlayer qlPlayer;
    QTablePlayer qTablePlayer;
    RandomMovesPlayer randomplayer;

    public GameOverScreen(Side winner,AIPlayer winnerPlayer) {
        this.winner = winner;
        this.winnerPlayer= winnerPlayer;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameOverScreen.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }

    @FXML
    void initialize() {
        resultLabel.setText(winner == WHITE ? "White wins!" : "Black wins!");
        if(winnerPlayer==basicAI) {
            winnerlabel.setText("BasicAIPlayer wins");
        }
        else if(winnerPlayer==expectiMiniMax)
        {
            winnerlabel.setText("ExpectiMinimaxPlayer wins");
        }
        else if(winnerPlayer==miniMaxPlayer)
        {
            winnerlabel.setText("MinimaxPlayer wins");
        }
        else if(winnerPlayer==qlPlayer)
        {
            winnerlabel.setText("qlPlayer wins");
        }
        else if(winnerPlayer==qTablePlayer)
        {
            winnerlabel.setText("qTablePlayer wins");
        }
        else
        {
            winnerlabel.setText("RandomPlayer wins");
        }
    }

    @FXML
    private Label resultLabel;

    @FXML
    private Label winnerlabel;

    @FXML
    private Button playAgainButton;

    @FXML
    private Button quitButton;

    @FXML
    void quit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void restart(ActionEvent event) throws IOException {
        /// TODO re-enable play again button
//        Stage stage = (Stage) playAgainButton.getScene().getWindow();
//        Parent root = new MainContainerController(MainContainerController.getInstance().type);
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
    }

    @FXML
    private Button historyButton;
    @FXML
    void historyButtonAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/viewData.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        stage.show();
    }

}
