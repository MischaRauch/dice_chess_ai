package logic;

import logic.enums.Side;

public class AIHandler {

    Side aiSide;

    public AIHandler(Side aiSide){
        this.aiSide = aiSide;
        System.out.println("AI WITH COLOR: " + aiSide.toString());
    }

    public Move getAIMove(){
        System.out.println("AI made a move");
        return null; //temp
    }

    public Side getAiSide() {
        return aiSide;
    }

    public void setAiSide(Side side){
        this.aiSide = side;
    }
}
//when it is the AIs turn to make the move -->  Game class calls AI.getAIMove
//player can only select its own pieces