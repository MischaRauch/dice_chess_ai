package simulation;

import logic.enums.Piece;
import logic.game.Game;
import logic.player.AIPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class SimulatorState extends Game {
    ArrayList<String> statsState = new ArrayList<String>();

    private final AIPlayer white, black;
    String whitePlayer;
    String blackPlayer;
    boolean whiteWon = false;

    ArrayList<Long> timeperMoveWhite = new ArrayList<Long>();
    ArrayList<Long> timeperMoveBlack = new ArrayList<Long>();
    private int numTurns;
    //check if a piece got captures
    private ArrayList<int[]> capturePieceArrayW;
    private ArrayList<int[]> capturePieceArrayB;
    //trackes the pieces of the board
    private ArrayList<int[]> pieceArrayW;
    private ArrayList<int[]> pieceArrayB;

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

    public void setTimeperMoveBlack(ArrayList<Long> timeperMoveBlack) {
        this.timeperMoveBlack = timeperMoveBlack;
    }

    public void setNumTurns(int numTurns) {
        this.numTurns = numTurns;
    }

    public void setCapturePieceArraysWhite(ArrayList<int[]> pieceArrayW, ArrayList<int[]> pieceArrayB) {
        this.capturePieceArrayW = pieceArrayW;
        this.capturePieceArrayB = pieceArrayB;
    }

    public void setPieceArraysWhite(ArrayList<int[]> pieceArrayW, ArrayList<int[]> pieceArrayB) {
        this.pieceArrayW = pieceArrayW;
        this.pieceArrayB = pieceArrayB;
    }

    public ArrayList<String> startStateSimulation(boolean turn, boolean timePerMove, boolean numCaptures, boolean whitePiecesRemaining, boolean blackPiecesRemaining) {
        boolean gameOver = false;
        AIPlayer nextPlayer = white;
        //Debugging
        System.out.println("Num turns: " + numTurns);
        System.out.println(timeperMoveWhite);
        System.out.println(timeperMoveBlack);


        //num captures, white pieces remaining on board
        //Names of pieces remaining on board
        if ((numTurns % 2) != 0) {
            whiteWon = true;
        }

        for (int i = 0; i < timeperMoveBlack.size(); i++) {

            statsState.add(getAIPlayerWhite().getNameAi());
            statsState.add(Long.toString(timeperMoveWhite.get(i)));
            if (i != 0) {
                statsState.add(checkPieceDeath(capturePieceArrayB.get((i) - 1), capturePieceArrayB.get(i), "BLACK"));
            } else
                statsState.add("-"); //first Move
            statsState.add(Arrays.toString(pieceArrayW.get(i)));

            statsState.add(getAIPlayerBlack().getNameAi());
            statsState.add(Long.toString(timeperMoveBlack.get(i)));
            if (i != 0) {
                statsState.add(checkPieceDeath(capturePieceArrayW.get((i) - 1), capturePieceArrayW.get(i), "WHITE"));
            } else
                statsState.add("-"); //first Move
            statsState.add(Arrays.toString(pieceArrayB.get(i)));

        }

        if (whiteWon) {
            statsState.add(getAIPlayerWhite().getNameAi());
            statsState.add(Long.toString(timeperMoveWhite.get(timeperMoveWhite.size() - 1)));
            statsState.add(checkPieceDeath(capturePieceArrayB.get(capturePieceArrayB.size() - 2), capturePieceArrayB.get(capturePieceArrayB.size() - 1), "BLACK"));
            statsState.add(Arrays.toString(pieceArrayW.get(capturePieceArrayW.size() - 1)));
        }

        return statsState;
    }

    public String checkPieceDeath(int[] previousBoard, int[] currentBoard, String color){
        for(int i = 0; i < previousBoard.length; i++){
            if(currentBoard[i] < previousBoard[i]){
                return color + " " + Piece.getPieceBasedOnNumber(i).name();
            }
        }
        return "-"; //safety
    }
}
