package dataCollection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import gui.controllers.viewDataController;

public class CsvReaderWriter extends viewDataController {

    public static void writeCsv(String filePath){

        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(filePath);
            fileWriter.append("Id, Game Type, Alg, Winner, Turns \n");
            //TODO
            for(GameInfo g : gameList){
                fileWriter.append(String.valueOf(g.getGameID()));
                fileWriter.append(",");
                fileWriter.append(g.getGameType());
                fileWriter.append(",");
                fileWriter.append(g.getAlgUsed());
                fileWriter.append(",");
                fileWriter.append(g.getGameWinner());
                fileWriter.append(",");
                fileWriter.append(String.valueOf(g.getNumTurnsToWin()));
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

    public static void readCsv(String filePath){
        BufferedReader reader = null;

        try{
            String line = "";
            reader = new BufferedReader(new FileReader(filePath));
            reader.readLine();

            while((line = reader.readLine()) != null){
                String[] fields = line.split(",");

                if(fields.length > 0){
                    GameInfo info = new GameInfo();
                    info.setGameID(Integer.parseInt(fields[0]));
                    info.setGameType(fields[1]);
                    info.setAlgUsed(fields[2]);
                    info.setGameWinner(fields[3]);
                    info.setNumTurnsToWin(Integer.parseInt(fields[4]));

                    gameList.add(info);
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
}
