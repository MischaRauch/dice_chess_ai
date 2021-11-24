package logic.game;

import logic.enums.Piece;
import logic.enums.Validity;
import gui.controllers.MainContainerController;
import gui.ChessIcons;
import gui.Chessboard;
import javafx.application.Platform;
import logic.Move;
import logic.State;
import logic.player.AIPlayer;

public class AiAiGame extends Game {

    private final AIPlayer white, black;
    private int playTill = 2;
    private int played = 0;

    public AiAiGame(AIPlayer white, AIPlayer black) {
        super(openingFEN);
        this.white = white;
        this.black = black;
    }
    public AiAiGame(AIPlayer white, AIPlayer black, int played) {
        super(openingFEN);
        this.white = white;
        this.black = black;
        this.played = played;
    }

    public void start() {
        boolean gameOver = false;
        AIPlayer nextPlayer = white;
        MainContainerController.inputBlock = true;    //prevents user from clicking dice roll button
        while (!gameOver) {
            Move move = nextPlayer.chooseMove(currentState);
            State newState = currentState.applyMove(move);
            previousStates.push(currentState);
            currentState = newState;
            move.setStatus(Validity.VALID);

            processCastling();

            //update GUI, need to use Platform.runLater because we are in a separate thread here,
            //and the GUI can only be updated from the main JavaFX thread. So we queue the GUI updates here
            Platform.runLater(() -> {
                Chessboard.chessboard.updateGUI(move);
                MainContainerController.getInstance().setDiceImage(ChessIcons.load(move.getDiceRoll(), move.getSide()));
            });

            //exit the loop on logic.game over
            if (currentState.getGameOver() != 0)
                gameOver = true;

            //switch players
            nextPlayer = (nextPlayer == white) ? black : white;

            //this part just adds a pause between moves so you can watch the logic.game instead of it instantly being over
            // main thread sleeps
            try {
                // the higher the depth the more time AI needs or game just freezes
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n\n\nGameOver\n\n\n");

        if (played <= playTill) {
            AiAiGame game = new AiAiGame(this.white, this.black, played +1);
            currentState.gameOver = 0;
            game.start();
        }

    }
}
