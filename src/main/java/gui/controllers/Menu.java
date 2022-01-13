package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.Config;
import logic.enums.GameType;
import logic.enums.Side;
import logic.game.AIGame;
import logic.game.AiAiGame;
import logic.game.HumanGame;
import logic.player.*;
import simulation.SimulationHandler;

import java.awt.*;
import java.io.IOException;

import static logic.enums.Side.BLACK;
import static logic.enums.Side.WHITE;

public class Menu {

    private final static String[] PLAYERS = {"Human", "Random AI", "Basic AI", "MiniMax AI", "QTable AI", "ExpectiMiniMax AI", "QL AI", "Hybrid_ExpectiQL AI"};
    private String openingFEN = Config.OPENING_FEN;
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
    private Button startButton;

    @FXML
    private Button infoButton;

    @FXML
    private TextField fenText;

    private static boolean singleGame;

    public static boolean getSingleGame() {
        return singleGame;
    }

    @FXML
    void initialize() {
        whitePlayerChoice.getItems().addAll(PLAYERS);
        blackPlayerChoice.getItems().addAll(PLAYERS);
        //set default game matchup
        whitePlayerChoice.setValue("ExpectiMiniMax AI");
        blackPlayerChoice.setValue("Random AI");
    }

    @FXML
    void start(ActionEvent event) {

        String whitePlayer = whitePlayerChoice.getValue();
        String blackPlayer = blackPlayerChoice.getValue();


        if(!(fenText.getText().isEmpty() || fenText.getText().isBlank() || fenText.getText() == "")){
            openingFEN = fenText.getText();
        }

        System.out.println("openingFEN: " + openingFEN);
        System.out.println("fenText: " + fenText.getText());

        GameType type;
        try{
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
                if (singleGameOption.isSelected()) {
                    singleGame = true;
                    new AiAiGame(white, black, openingFEN);
                } else {
                    SimulationHandler sH = new SimulationHandler(white, black, openingFEN, simulationOption, singleGameOption);
                    sH.startHandler();
                }

                }
                case HUMAN_V_AI -> {
                    AIPlayer aiPlayer = getPlayer(blackPlayer, BLACK);
                    new AIGame(aiPlayer,openingFEN);
                }
                case HUMAN_V_HUMAN -> {
                    new HumanGame(openingFEN);
                }
                default -> new HumanGame(openingFEN);
            }

            try {
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                MainContainerController.stage = stage;
                Parent root = new MainContainerController(type, openingFEN);
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            System.out.println("Invalid FEN");
            this.fenText.setText("");
            this.openingFEN = Config.OPENING_FEN;
            start(event);
        }
    }

    private AIPlayer getPlayer(String player, Side color) {
        return switch (player) {
            case "Random AI" -> new RandomMovesPlayer(color);
            case "Basic AI" -> new BasicAIPlayer(color);
            case "MiniMax AI" -> new MiniMaxPlayer(7, color);
            case "QTable AI" -> new QTablePlayer(color);
            case "QL AI" -> new QLPlayer(2, color);
            case "ExpectiMiniMax AI" -> new ExpectiMiniMaxPlayer(3,color);
            case "Hybrid_ExpectiQL AI" -> new Hybrid_ExpectiQL(2,color);
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
    void viewInfo(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/instructions.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        stage.show();
    }



}
