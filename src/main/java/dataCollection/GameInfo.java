package dataCollection;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class GameInfo {

    public SimpleIntegerProperty gameID = new SimpleIntegerProperty();
    public SimpleStringProperty gameType = new SimpleStringProperty();
    public SimpleStringProperty algUsed = new SimpleStringProperty();
    public SimpleStringProperty gameWinner = new SimpleStringProperty();
    public SimpleIntegerProperty numTurnsToWin = new SimpleIntegerProperty();
    //public SimpleDoubleProperty timeToWin = new SimpleDoubleProperty();

    public GameInfo(){}

    public GameInfo(SimpleStringProperty gameType, SimpleStringProperty algUsed, SimpleStringProperty gameWinner, SimpleIntegerProperty numTurnsToWin) {
        this.gameType = gameType;
        this.algUsed = algUsed;
        this.gameWinner = gameWinner;
        this.numTurnsToWin = numTurnsToWin;
    }

    public int getGameID(){return this.gameID.get();}
    public void setGameID(int id){this.gameID.set(id);}

    public String getGameType(){return this.gameType.get();}
    public void setGameType(String type){this.gameType.set(type);}

    public String getAlgUsed(){return this.algUsed.get();}
    public void setAlgUsed(String alg){this.algUsed.set(alg);}

    public String getGameWinner(){return this.gameWinner.get();}
    public void setGameWinner(String winner){this.gameWinner.set(winner);}

    public int getNumTurnsToWin(){return this.numTurnsToWin.get();}
    public void setNumTurnsToWin(int num){this.numTurnsToWin.set(num);}

    /*public Double getTimeToWin(){return this.timeToWin.get();}
    public void setTimeToWin(double time){this.timeToWin.set(time);}
     */

}
