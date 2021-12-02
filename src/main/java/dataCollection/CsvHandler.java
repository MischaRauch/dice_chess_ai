package dataCollection;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import gui.controllers.ViewDataController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URISyntaxException;

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
                    CsvHandler csv = new CsvHandler();
                    csv.setAlgSide(fields[0]);
                    csv.setTimeElapsed(Integer.parseInt(fields[1]));
                    aiTimeList.add(csv);
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
        aiTimeList.add(this);

        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(filePath);
            fileWriter.append("Side, TimeElapsed \n");

            for(CsvHandler c : aiTimeList){
                fileWriter.append(c.getAlgSide());
                fileWriter.append(",");
                fileWriter.append(String.valueOf(c.getTimeElapsed()));
                fileWriter.append("\n");
            }

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
