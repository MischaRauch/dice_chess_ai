package simulation;

import logic.Move;
import logic.State;
import logic.enums.Validity;
import logic.game.Game;
import logic.player.AIPlayer;

import java.util.ArrayList;

public class SimulatorState extends Game {
    ArrayList<String> statsState = new ArrayList<String>();

    private final AIPlayer white, black;
    String whitePlayer;
    String blackPlayer;
    String tmp;

    ArrayList<Long> timeperMoveWhite = new ArrayList<Long>();
    ArrayList<Long> timeperMoveBlack = new ArrayList<Long>();
    private int numTurns;

    public SimulatorState(AIPlayer white, AIPlayer black, String FEN) {
        super(FEN);
        this.white = white;
        this.black = black;
        whitePlayer = white.getNameAi();
        blackPlayer = black.getNameAi();
    }

    public AIPlayer getAIPlayerWhite() {
        return white;
    }

    public AIPlayer getAIPlayerBlack() {
        return black;
    }

    public void setTimeperMoveWhite(ArrayList<Long> timeperMoveWhite){
        this.timeperMoveWhite = timeperMoveWhite;
    }

    public void setTimeperMoveBlack(ArrayList<Long> timeperMoveBlack){
        this.timeperMoveBlack = timeperMoveBlack;
    }

    public void setNumTurns(int numTurns){
        this.numTurns = numTurns;
    }

    public ArrayList<String> startStateSimulation(boolean turn, boolean timePerMove, boolean numCaptures, boolean whitePiecesRemaining, boolean blackPiecesRemaining){
        boolean gameOver = false;
        AIPlayer nextPlayer = white;
        //Debugging
        System.out.println("Num turns: " + numTurns);
        System.out.println(timeperMoveWhite);
        System.out.println(timeperMoveBlack);

        int lessTurns = 0;
        if(timeperMoveBlack.size() < timeperMoveWhite.size())
            lessTurns = timeperMoveBlack.size();
        else
            lessTurns = timeperMoveWhite.size();

        for(int i = 0; i < lessTurns; i++){
            statsState.add(getAIPlayerWhite().getNameAi());
            statsState.add(Long.toString(timeperMoveWhite.get(i)));

            statsState.add(getAIPlayerBlack().getNameAi());
            statsState.add(Long.toString(timeperMoveBlack.get(i)));
        }

        if(lessTurns == timeperMoveBlack.size()){
            statsState.add(getAIPlayerWhite().getNameAi());
            statsState.add(Long.toString(timeperMoveWhite.get(timeperMoveWhite.size() -1)));
        }
        else{
            statsState.add(getAIPlayerBlack().getNameAi());
            statsState.add(Long.toString(timeperMoveBlack.get(timeperMoveBlack.size()-1)));
        }

        return statsState;
    }
}
