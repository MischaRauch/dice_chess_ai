package dataCollection;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import gui.controllers.viewDataController;

public class csvHandler extends viewDataController{
    private SimpleStringProperty type;
    private SimpleStringProperty alg;
    private SimpleStringProperty winner;
    private SimpleIntegerProperty turns;


    public csvHandler(){}

    public csvHandler(String type, String alg, String winner, int turns) {
        this.type = new SimpleStringProperty(type);
        this.alg = new SimpleStringProperty(alg);
        this.winner = new SimpleStringProperty(winner);
        this.turns = new SimpleIntegerProperty(turns);
    }

    public void addToCsv(){
        GameInfo newGame = new GameInfo(type, alg, winner, turns);
        gameList.add(newGame);
        System.out.println(gameList);
        CsvReaderWriter.writeCsv("data.csv");
        System.out.println("ADDED TO CSV");
    }

    public void readTheCsv(){
        CsvReaderWriter.readCsv("data.csv");
    }
}
