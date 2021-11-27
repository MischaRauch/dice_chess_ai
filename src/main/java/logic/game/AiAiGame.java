package logic.game;

import dataCollection.CsvHandler;
import logic.Config;
import logic.enums.Side;
import logic.enums.Validity;
import gui.controllers.MainContainerController;
import gui.ChessIcons;
import gui.Chessboard;
import javafx.application.Platform;
import logic.Move;
import logic.State;
import logic.player.AIPlayer;
import logic.player.MiniMaxPlayer;
import logic.player.QTablePlayer;

import static logic.enums.Side.*;

public class AiAiGame extends Game {

    private final AIPlayer white, black;
    //will play AIvsAI 50 times
    private int playTill = Config.SIMULATION_SIZE;
    private int played = 0;
    private CsvHandler handle;


    public AiAiGame(AIPlayer white, AIPlayer black) {
        super();
        this.white = white;
        this.black = black;
    }
    public AiAiGame(AIPlayer white, AIPlayer black, int played) {
        super();
        this.white = white;
        this.black = black;
        this.played = played;
    }

    public AIPlayer getAIPlayerWhite(){return white;}
    public AIPlayer getAIPlayerBlack(){return black;}

    public void start() {
        boolean gameOver = false;
        AIPlayer nextPlayer = white;
        MainContainerController.inputBlock = true;    //prevents user from clicking dice roll button
        while (!gameOver) {
            Move move = nextPlayer.chooseMove(currentState);
            State newState = currentState.applyMove(move);
            previousStates.push(currentState);

            //need to check if the destination capture move was a king, and in the next state the state the king might
            //be dead already. so we can't check it was captured
            checkGameOver(move);

            //after checking if king was captured, we can updated the currentState
            currentState = newState;
            move.setStatus(Validity.VALID);

            processCastling();

            //update GUI, need to use Platform.runLater because we are in a separate thread here,
            //and the GUI can only be updated from the main JavaFX thread. So we queue the GUI updates here
            Platform.runLater(() -> {
                Chessboard.chessboard.updateGUI(move);
                MainContainerController.getInstance().setDiceImage(ChessIcons.load(move.getDiceRoll(), move.getSide()));
                MainContainerController.getInstance().updateTurn(move.getSide());
            });

            //update the value for gameOver,so we eventually exit this loop
            gameOver = isGameOver();

            //switch players
            nextPlayer = (nextPlayer == white) ? black : white;

            //this part just adds a pause between moves so you can watch the logic.game instead of it instantly being over
            // main thread sleeps
            try {
                // the higher the depth the more time AI needs or game just freezes
                Thread.sleep(Config.THREAD_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n\n\nGameOver\n\n\n");

        System.out.println("PLAYED: "+played+ " PLAY TILL: "+playTill);

        if (played < playTill) {
            //AiAiGame game = new AiAiGame(new BasicAIPlayer(WHITE), this.black, played +1);
            AiAiGame game = new AiAiGame(new QTablePlayer(WHITE), new MiniMaxPlayer(10,BLACK), played +1);
            game.start(); //Question: does this create a new thread for every game run? like do we ever close the previous threads when the game is finished?
            updateCsvFile(game);
        }

    }

    public void updateCsvFile(AiAiGame game ) {
        //I prefer game over to not be an integer property of the State class but instead a Side from the Game class
        //Side winner = game.getCurrentState().getGameOver() == 1 ? WHITE : BLACK; -> old version

        //better used this: since I've deleted all game over stuff from the State class:
        //Side winner = game.isGameOver() ? game.getWinner() : NEUTRAL;

        //do we even need to check if game is over at this point? Or does this method only run when games are finished?
        //in which case:
        Side winner = game.getWinner();
        handle = new CsvHandler(game.getAIPlayerWhite().getNameAi(), game.getAIPlayerBlack().getNameAi(), winner.name(), game.getPreviousStates().lastElement().getCumulativeTurn());
        handle.aiVsAiCsvWrite();
    }
}