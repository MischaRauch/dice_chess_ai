package logic.game;

import dataCollection.CsvHandler;
import logic.Config;
import logic.enums.Side;
import logic.enums.Validity;
import gui.controllers.MainContainerController;
import gui.ChessIcons;
import gui.Chessboard;
import gui.controllers.MainContainerController;
import javafx.application.Platform;
import logic.Config;
import logic.Move;
import logic.PieceAndSquareTuple;
import logic.State;
import logic.enums.Side;
import logic.enums.Validity;
import logic.player.AIPlayer;
import logic.player.BasicAIPlayer;
import logic.player.MiniMaxPlayer;
import logic.player.QTablePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AiAiGame extends Game {

    private final AIPlayer white, black;
    //will play AIvsAI 50 times
    private static int playTill = Config.SIMULATION_SIZE;
    private int played = 0;
    private CsvHandler handle;
    private static String[][] resultsArray = new String[playTill+1][4];


    public AiAiGame(AIPlayer white, AIPlayer black, String FEN) {
        super(FEN);
        this.white = white;
        this.black = black;
    }

    public AiAiGame(AIPlayer white, AIPlayer black, int played, String FEN) {
        super(FEN);
        this.white = white;
        this.black = black;
        this.played = played;
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
        // FIXED BUG need to clone first state as when you call restart method you launch same game which has same state,
        // so game gets loaded from the state that the previous game was loaded from
        System.out.println("AiAiGame; playTill: " + playTill);
        System.out.println("AiAiGame; Played: " + played);
        while (!gameOver) {
            System.out.println("AiAiGame; real turn: " + currentState.getCumulativeTurn() + " ");

            List<PieceAndSquareTuple> first = currentState.getPieceAndSquare();

            //update the value for gameOver,so we eventually exit this loop
            Move move = nextPlayer.chooseMove(currentState);

            // TODO MAKE evaluator legal move not modify the state for castling
            //  the state should track all castling not the evaluator
//            if (evaluator.isLegalMove(move, currentState, true, true)) {

            //need to check if the destination capture move was a king, and in the next state the state the king might
            //be dead already. so we can't check it was capture
            /// TODO FIX BUG (if FEN loaded with only 2 kings game freezes)

            State newState = currentState.applyMove(move);
            previousStates.push(currentState);
            checkGameOver(move);
            //after checking if king was captured, we can updated the currentState
            currentState = newState;
            move.setStatus(Validity.VALID);

            List<PieceAndSquareTuple> next = currentState.getPieceAndSquare();


            processCastling();


            //MainContainerController.getInstance().updateTurn(currentState.getColor());
//            } else {
//                move.setInvalid();
//            }
            //update GUI, need to use Platform.runLater because we are in a separate thread here,
            //and the GUI can only be updated from the main JavaFX thread. So we queue the GUI updates here
            Platform.runLater(() -> {
                Chessboard.chessboard.updateGUI(move);
                MainContainerController.getInstance().setDiceImage(ChessIcons.load(move.getDiceRoll(), move.getSide()));
                MainContainerController.getInstance().updateTurn(move.getSide());
            });

            //update the value for gameOver, updates gameDone in Game, so we eventually exit this loop
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


        //Save the information for this game
        resultsArray[played][0] = white.getNameAi();
        resultsArray[played][1] = black.getNameAi();
        resultsArray[played][2] = getWinner().name();
        resultsArray[played][3] = Integer.toString(previousStates.lastElement().getCumulativeTurn());
        //only add once all the data is gathered
        if(played == playTill) {
            loogerFromArray(resultsArray);
        }
        // reset current state to first state (first state initialized in game abstract class the first time game is initialized)
        currentState = firstState;

        //Csv
        CsvHandler csvHandler = new CsvHandler();
        csvHandler.readTimeCsv("time.csv");
        csvHandler.writeTimeCsv("time.csv");

        System.out.println("\n\n\nGameOver\n\n\n");
        System.out.println("PLAYED: " + played + " PLAY TILL: " + playTill);

        if (played < playTill) {
            AiAiGame game = new AiAiGame(this.white, this.black, played + 1, this.getFEN());
            game.start(); //Question: does this create a new thread for every game run? like do we ever close the previous threads when the game is finished?
            //updateCsvFile(game);
        }


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

    public void updateCsvFile(AiAiGame game) {
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

    public void loogerFromArray(String[][] array) {
        for (int i = 1; i <= played; i++) {
            handle = new CsvHandler(array[i][0], array[i][1], array[i][2], Integer.parseInt(array[i][3]));
            handle.aiVsAiCsvWrite();
        }
    }
}
