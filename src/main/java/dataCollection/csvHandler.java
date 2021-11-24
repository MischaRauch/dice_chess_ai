package dataCollection;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import gui.controllers.viewDataController;
import logic.enums.GameType;

public class csvHandler extends viewDataController{
    private SimpleStringProperty type;
    private SimpleStringProperty alg;
    private SimpleStringProperty algTwo;
    private SimpleStringProperty algSide;
    private SimpleStringProperty winner;
    private SimpleIntegerProperty turns;

    public csvHandler(){}

    public csvHandler(String type, String alg, String algSide, String winner, int turns) {
        this.type = new SimpleStringProperty(type);
        this.alg = new SimpleStringProperty(alg);
        this.algSide = new SimpleStringProperty(algSide);
        this.winner = new SimpleStringProperty(winner);
        this.turns = new SimpleIntegerProperty(turns);
    }

    public csvHandler(String alg, String algTwo, String winner, int turns){
        this.alg = new SimpleStringProperty(alg);
        this.algTwo = new SimpleStringProperty(algTwo);
        this.winner = new SimpleStringProperty(winner);
        this.turns = new SimpleIntegerProperty(turns);
    }

    public void addToCsv(){
        GameInfo newGame = new GameInfo(type, alg, algSide, winner, turns);
        gameList.add(newGame);
        CsvReaderWriter.writeCsv("data.csv");
    }

    public void readTheCsv(){
        CsvReaderWriter.readCsv("data.csv");
    }

    public void aiVsAiCsvRead(){
        AICsvReaderWriter.readCsv("aiVsAi.csv");
    }

    public void aiVsAiCsvWrite(){
        GameInfo aiAiGame = new GameInfo(alg, algTwo, winner, turns);
        aiAiGameList.add(aiAiGame);
        AICsvReaderWriter.writeCsv("aiVsAi.csv");
    }
}
