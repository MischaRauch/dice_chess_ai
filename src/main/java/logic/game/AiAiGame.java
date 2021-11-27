package logic.game;

import dataCollection.csvHandler;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Validity;
import gui.controllers.MainContainerController;
import gui.ChessIcons;
import gui.Chessboard;
import javafx.application.Platform;
import logic.Move;
import logic.State;
import logic.player.AIPlayer;
import logic.player.BasicAIPlayer;
import logic.player.MiniMaxPlayer;
import logic.player.QTablePlayer;

import static logic.enums.Side.BLACK;
import static logic.enums.Side.WHITE;

public class AiAiGame extends Game {

    private final AIPlayer white, black;
    //will play AIvsAI 50 times
    private int playTill = 49;
    private int played = 0;
    private csvHandler handle;


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

    public AIPlayer getAIPlayerWhite(){return white;}
    public AIPlayer getAIPlayerBlack(){return black;}

    public void start() {
        boolean gameOver = false;
        AIPlayer nextPlayer = white;
        MainContainerController.inputBlock = true;    //prevents user from clicking dice roll button
        while (!gameOver) {
            //update the value for gameOver,so we eventually exit this loop

            Move move = nextPlayer.chooseMove(currentState);
            System.out.println(move.toString());

            // TODO MAKE evaluator legal move not modify the state for castling
            //  the state should track all castling not the evaluator
            if (evaluator.isLegalMove(move, currentState, true,true)) {

            //need to check if the destination capture move was a king, and in the next state the state the king might
            //be dead already. so we can't check it was capture
             /// TODO FIX BUG (if FEN loaded with only 2 kings game freezes)
            checkGameOver(move);
            gameOver = isGameOver();

            State newState = currentState.applyMove(move);

            previousStates.push(currentState);
            //after checking if king was captured, we can updated the currentState
            currentState = newState;
            move.setStatus(Validity.VALID);
            processCastling();


            MainContainerController.getInstance().updateTurn(currentState.getColor());
            } else {
                move.setInvalid();
            }
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
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n\n\nGameOver\n\n\n");

        System.out.println("PLAYED: "+played+ " PLAY TILL: "+playTill);

        if (played < playTill) {
            //AiAiGame game = new AiAiGame(new BasicAIPlayer(WHITE), this.black, played +1);
            AiAiGame game = new AiAiGame(this.white, this.black, played +1, this.getFEN());
            //currentState.setGameOver(0);
            game.start(); //Question: does this create a new thread for every game run? like do we ever close the previous threads when the game is finished?
            updateCsvFile(game);
        }

    }

    public void updateCsvFile(AiAiGame game ) {
        Side winner = game.getCurrentState().getGameOver() == 1 ? WHITE : BLACK;
        handle = new csvHandler(game.getAIPlayerWhite().getNameAi(), game.getAIPlayerBlack().getNameAi(), winner.name(), game.getPreviousStates().lastElement().getCumulativeTurn());
        handle.aiVsAiCsvWrite();
    }
}
