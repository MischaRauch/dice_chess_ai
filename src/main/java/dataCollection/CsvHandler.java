package dataCollection;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import gui.controllers.ViewDataController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class CsvHandler extends ViewDataController {
    private SimpleStringProperty type;
    private SimpleStringProperty alg;
    private SimpleStringProperty algTwo;
    private SimpleStringProperty algSide;
    private SimpleStringProperty winner;
    private SimpleIntegerProperty turns;

    private SimpleLongProperty timeElapsed;

    public CsvHandler(){}
    //For everything else
    public CsvHandler(String type, String alg, String algSide, String winner, int turns) {
        this.type = new SimpleStringProperty(type);
        this.alg = new SimpleStringProperty(alg);
        this.algSide = new SimpleStringProperty(algSide);
        this.winner = new SimpleStringProperty(winner);
        this.turns = new SimpleIntegerProperty(turns);
    }
    //For AIAI
    public CsvHandler(String alg, String algTwo, String winner, int turns){
        this.alg = new SimpleStringProperty(alg);
        this.algTwo = new SimpleStringProperty(algTwo);
        this.winner = new SimpleStringProperty(winner);
        this.turns = new SimpleIntegerProperty(turns);
    }
    //For Time
    public CsvHandler(String algSide, long timeElapsed){
        this.algSide = new SimpleStringProperty(algSide);
        this.timeElapsed = new SimpleLongProperty(timeElapsed);
    }

    public String getAlgSide() {return algSide.get();}

    public void setAlgSide(String algSide) {this.algSide.set(algSide);}

    public long getTimeElapsed() {return timeElapsed.get();}

    public void setTimeElapsed(int timeElapsed) {this.timeElapsed.set(timeElapsed);}

    public void addToCsv(){
        GameInfo newGame = new GameInfo(type, alg, algSide, winner, turns);
        gameList.add(newGame);
        CsvReaderWriter.writeCsv("data.csv");
    }

    public void readTheCsv(){
        CsvReaderWriter.readCsv("data.csv");
    }

    public void aiVsAiCsvRead() {
        AICsvReaderWriter.readCsv("aiVsAi.csv");
    }

    public void aiVsAiCsvWrite(){
        GameInfo aiAiGame = new GameInfo(alg, algTwo, winner, turns);
        aiAiGameList.add(aiAiGame);
        AICsvReaderWriter.writeCsv("aiVsAi.csv");
    }


    public void readTimeCsv(String filePath){
        BufferedReader reader = null;

        try{
            String line = "";
            reader = new BufferedReader(new FileReader(filePath));
            reader.readLine();

            while((line = reader.readLine()) != null){
                String[] fields = line.split(",");

                if(fields.length > 0){
                    for(int i = 0; i < fields.length; i++){
                        CsvHandler csvHandler = new CsvHandler(); //TODO: check
                        csvHandler.setTimeElapsed(Integer.parseInt((fields[0])));
                        csvHandler.setTimeElapsed(Integer.parseInt((fields[1])));
                        csvHandler.setTimeElapsed(Integer.parseInt((fields[2])));
                        csvHandler.setTimeElapsed(Integer.parseInt((fields[3])));
                        csvHandler.setTimeElapsed(Integer.parseInt((fields[4])));
                        csvHandler.setTimeElapsed(Integer.parseInt((fields[5])));
                        csvHandler.setTimeElapsed(Integer.parseInt((fields[6])));
                        csvHandler.setTimeElapsed(Integer.parseInt((fields[7])));
                        csvHandler.setTimeElapsed(Integer.parseInt((fields[8])));
                        csvHandler.setTimeElapsed(Integer.parseInt((fields[9])));
                        aiTimeList.add(csvHandler);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void writeTimeCsv(String filePath){

        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(filePath);
            fileWriter.append("Game 1, Game 2, Game 3, Game 4, Game 5, Game 6, Game 7, Game 8, Game 9, Game 10 \n");

            for(CsvHandler c : aiTimeList){
                fileWriter.append(String.valueOf(c.getTimeElapsed()));
                fileWriter.append(",");
            }
            fileWriter.append("'");

        } catch(Exception ex){
            ex.printStackTrace();
        } finally {
            try{
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
