package dataCollection;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import gui.controllers.ViewDataController;

public class CsvHandler extends ViewDataController {
    private SimpleStringProperty type;
    private SimpleStringProperty alg;
    private SimpleStringProperty algTwo;
    private SimpleStringProperty algSide;
    private SimpleStringProperty winner;
    private SimpleIntegerProperty turns;

    public CsvHandler(){}

    public CsvHandler(String type, String alg, String algSide, String winner, int turns) {
        this.type = new SimpleStringProperty(type);
        this.alg = new SimpleStringProperty(alg);
        this.algSide = new SimpleStringProperty(algSide);
        this.winner = new SimpleStringProperty(winner);
        this.turns = new SimpleIntegerProperty(turns);
    }

    public CsvHandler(String alg, String algTwo, String winner, int turns){
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
