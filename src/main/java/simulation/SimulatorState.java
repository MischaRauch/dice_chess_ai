package simulation;

import logic.enums.Piece;
import logic.enums.Side;
import logic.game.Game;
import logic.player.AIPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class SimulatorState extends Game {
    ArrayList<String> statsState = new ArrayList<String>();

    private final AIPlayer white, black;
    String whitePlayer;
    String blackPlayer;
    String tmp;

    ArrayList<Long> timeperMoveWhite = new ArrayList<Long>();
    ArrayList<Long> timeperMoveBlack = new ArrayList<Long>();
    private int numTurns;
    private ArrayList<int[]> pieceArrayW;
    private ArrayList<int[]> pieceArrayB;
    //ArrayList<Piece> pieceDeathList = new ArrayList<>();//check if we need a number (if multiple deaths at once can occur)

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

    public void setPieceArrays(ArrayList<int[]> pieceArrayW, ArrayList<int[]> pieceArrayB){
        this.pieceArrayW = pieceArrayW;
        this.pieceArrayB = pieceArrayB;
        for (int[] state : pieceArrayW)
            System.out.println("WHITE FROM LOGGER " + Arrays.toString(state));
        System.out.println();
        for (int[] state : pieceArrayB)
            System.out.println("BLACK FROM LOGGER " + Arrays.toString(state));
    }

    public ArrayList<String> startStateSimulation(){

        //Debugging
        System.out.println("Num turns: " + numTurns);
        System.out.println(timeperMoveWhite);
        System.out.println(timeperMoveBlack);


        int lessTurns = 0;
        if(timeperMoveBlack.size() < timeperMoveWhite.size())
            lessTurns = timeperMoveBlack.size();
        else
            lessTurns = timeperMoveWhite.size();

        System.out.println("TimePerMWhite: " + timeperMoveWhite.size());
        System.out.println("TimePerMBlack: " + timeperMoveBlack.size());
        System.out.println("LESS TURNS: " + lessTurns);

        for(int i = 0; i < lessTurns; i++){
            System.out.println("II " + i);
            statsState.add(getAIPlayerWhite().getNameAi());
            statsState.add(Long.toString(timeperMoveWhite.get(i)));
            if(i != 0 ){
                statsState.add(checkPieceDeath(pieceArrayB.get(i-1), pieceArrayB.get(i), "BLACK"));
            }
            else
                statsState.add("-"); //first Move
            statsState.add(Arrays.toString(pieceArrayW.get(i)));
            System.out.println("TO FILE WHITE " + Arrays.toString(pieceArrayW.get(i)));

            statsState.add(getAIPlayerBlack().getNameAi());
            statsState.add(Long.toString(timeperMoveBlack.get(i)));
            if(i != lessTurns-1 ){//?
                statsState.add(checkPieceDeath(pieceArrayW.get(i), pieceArrayW.get(i+1), "WHITE"));
            }
            else
                statsState.add("-"); //first Move //CHECK THIS
            statsState.add(Arrays.toString(pieceArrayB.get(i)));
            System.out.println("TO FILE BLACK " + Arrays.toString(pieceArrayB.get(i)));
        }

        if(lessTurns == timeperMoveBlack.size() && lessTurns != timeperMoveWhite.size()){ //Will the White Array every have less turns? or only equal or more
            System.out.println("THIS IS RUNNING");
            statsState.add(getAIPlayerWhite().getNameAi());
            statsState.add(Long.toString(timeperMoveWhite.get(timeperMoveWhite.size() -1)));
            //statsState.add("TPM DONE");
            statsState.add(checkPieceDeath(pieceArrayB.get(pieceArrayB.size()-2), pieceArrayB.get(pieceArrayB.size()-1), "BLACK"));
            //statsState.add("CAP DONE");
            statsState.add(Arrays.toString(pieceArrayW.get(pieceArrayW.size()-1)));
            //statsState.add("ARRAY DONE");

            statsState.add(getAIPlayerBlack().getNameAi());
            statsState.add("-");
            statsState.add("-");
            statsState.add(Arrays.toString(pieceArrayB.get(pieceArrayB.size()-1))); //CHECK THIS
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
