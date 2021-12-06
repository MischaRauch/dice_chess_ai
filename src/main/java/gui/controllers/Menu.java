package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.Config;
import logic.enums.GameType;
import logic.enums.Side;
import logic.game.AIGame;
import logic.game.AiAiGame;
import logic.game.HumanGame;
import logic.player.*;

import java.io.IOException;

import static logic.enums.Side.BLACK;
import static logic.enums.Side.WHITE;

public class Menu {

    private final static String[] PLAYERS = {"Human", "Random AI", "Basic AI", "MiniMax AI", "QTable AI", "ExpectiMiniMax AI", "QL AI"};

    @FXML
    private ChoiceBox<String> whitePlayerChoice;

    @FXML
    private ChoiceBox<String> blackPlayerChoice;

    @FXML
    private VBox aiMatchConfigBox;

    @FXML
    private RadioButton singleGameOption;

    @FXML
    private ToggleGroup aiMatchType;

    @FXML
    private TextField delayInput;

    @FXML
    private RadioButton simulationOption;

    @FXML
    private TextField iterationsInput;

    @FXML
    private Button statsButton;

    @FXML
    private Button startButton;

    @FXML
    void initialize() {
        whitePlayerChoice.getItems().addAll(PLAYERS);
        blackPlayerChoice.getItems().addAll(PLAYERS);
        //set default game matchup
        whitePlayerChoice.setValue("MiniMax AI");
        blackPlayerChoice.setValue("ExpectiMiniMax AI");
    }

    @FXML
    void start(ActionEvent event) {

        String whitePlayer = whitePlayerChoice.getValue();
        String blackPlayer = blackPlayerChoice.getValue();

        GameType type;
        if (whitePlayer.equals("Human") && blackPlayer.equals("Human")) {
            //Human vs Human game
            type = GameType.HUMAN_V_HUMAN;
        } else if (!whitePlayer.equals("Human") && !blackPlayer.equals("Human")) {
            //AI vs AI
            type = GameType.AI_V_AI;

            if (aiMatchType.getSelectedToggle() == singleGameOption) {
                // setting to 0 to fix turn bug
                Config.SIMULATION_SIZE = 0;
                Config.THREAD_DELAY = Integer.parseInt(delayInput.getText()); //TODO sanitize input so only integers are accepted
            } else {
                Config.SIMULATION_SIZE = Integer.parseInt(iterationsInput.getText());
                Config.THREAD_DELAY = 1;
            }

        } else if (!blackPlayer.equals("Human")){
            //white is human and black is AI
            type = GameType.HUMAN_V_AI;
        } else {
            //white is AI and black is Human
            type = GameType.HUMAN_V_AI;
        }

        switch (type) {
            case AI_V_AI -> {
                AIPlayer white = getPlayer(whitePlayer, WHITE);
                AIPlayer black = getPlayer(blackPlayer, BLACK);
                new AiAiGame(white, black, Config.OPENING_FEN);
            }
            case HUMAN_V_AI -> {
                AIPlayer aiPlayer = getPlayer(blackPlayer, BLACK);
                new AIGame(aiPlayer,Config.OPENING_FEN);
            }
            case HUMAN_V_HUMAN -> {
                new HumanGame(Config.OPENING_FEN);
            }
            default -> new HumanGame(Config.OPENING_FEN);
        }

        try {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            MainContainerController.stage = stage;
            Parent root = new MainContainerController(type);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AIPlayer getPlayer(String player, Side color) {
        return switch (player) {
            case "Random AI" -> new RandomMovesPlayer(color);
            case "Basic AI" -> new BasicAIPlayer(color);
            case "MiniMax AI" -> new MiniMaxPlayer(7, color);
            case "QTable AI" -> new QTablePlayer(color);
            case "QL AI" -> new QLPlayer(2, color);
            case "ExpectiMiniMax AI" -> new ExpectiMiniMaxPlayer(9,color);
            default -> new RandomMovesPlayer(color);
        };
    }

    @FXML
    void disableSimulation(ActionEvent event) {

    }

    @FXML
    void disableSingleGame(ActionEvent event) {

    }

    @FXML
    void displayStats(ActionEvent event) {

    }



}
