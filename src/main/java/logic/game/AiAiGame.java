package logic.game;


import gui.ChessIcons;
import gui.Chessboard;
import gui.controllers.MainContainerController;
import javafx.application.Platform;
import logic.Config;
import logic.Move;
import logic.PieceAndSquareTuple;
import logic.State;
import logic.enums.Validity;
import logic.player.AIPlayer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AiAiGame extends Game {
    private final AIPlayer white, black;


    public AiAiGame(AIPlayer white, AIPlayer black, String FEN) {
        super(FEN);
        this.white = white;
        this.black = black;
    }


    public AIPlayer getAIPlayerWhite() {
        return white;
    }

    public AIPlayer getAIPlayerBlack() {
        return black;
    }

    public void start() {
        boolean gameOver = false;
        AIPlayer nextPlayer = white;
        MainContainerController.inputBlock = true;    //prevents user from clicking dice roll button
        // need to clone first state as when you call restart method you launch same game which has same state,
        // so game gets loaded from the state that the previous game was loaded from

        while (!gameOver) {

            Move move = nextPlayer.chooseMove(currentState);

            checkGameOver(move);
            //update the value for gameOver, updates gameDone in Game, so we eventually exit this loop
            gameOver = isGameOver();

            //currentState.printPieceAndSquare();

            State newState = currentState.applyMove(move); //AFTER THIS LINE NOTHING EXECUTES
            // DICE ROLL IS 0

            //currentState.printPieceAndSquare();


            previousStates.push(currentState);

            // after checking if king was captured, we can update the currentState
            currentState = newState;

            //System.out.println("Updated State: ");
            //currentState.printPieceAndSquare();

            move.setStatus(Validity.VALID);

            processCastling();

            //update GUI, need to use Platform.runLater because we are in a separate thread here,
            //and the GUI can only be updated from the main JavaFX thread. So we queue the GUI updates here
            Platform.runLater(() -> {
                Chessboard.chessboard.updateGUI(move);
                MainContainerController.getInstance().setDiceImage(ChessIcons.load(move.getDiceRoll(), move.getSide()));
                MainContainerController.getInstance().updateTurn(move.getSide());
            });



            //switch players
            nextPlayer = (nextPlayer == white) ? black : white;

            //this part just adds a pause between moves so you can watch the logic.game instead of it instantly being over
            // main thread sleeps
            try {
                // the higher the depth the more time AI needs or game just freezes
                Thread.sleep(Config.THREAD_DELAY);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        // reset current state to first state (first state initialized in game abstract class the first time game is initialized)
        currentState = firstState;

        System.out.println("\n\n\nGameOver\n\n\n");
    }

    public boolean isEqualState(List<PieceAndSquareTuple> first, List<PieceAndSquareTuple> second) {
        List<PieceAndSquareTuple> firstCopy = first.stream().collect(Collectors.toList());

        if (first.size() == second.size()) {
            int size = first.size();
            for (int i = 0; i < size; i++) {
                int count = 0;
                for (int j = 0; j < size; j++) {
                    if (firstCopy.get(i).getPiece() == second.get(j).getPiece() && firstCopy.get(i).getSquare() == second.get(j).getSquare()) {
                        count++;
                    }
                    Collections.rotate(firstCopy, 1);
                }
                if (count==size) {
                    return true;
                }
            }
        }
        return false;
    }
}
