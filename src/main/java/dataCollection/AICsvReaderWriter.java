package dataCollection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import gui.controllers.viewDataController;

public class AICsvReaderWriter extends viewDataController {

    public static void writeCsv(String filePath){

        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(filePath);
            fileWriter.append("Alg, AlgTwo, Winner, Turns \n");

            for(GameInfo g : aiAiGameList){
                fileWriter.append(g.getAlgUsed());
                fileWriter.append(",");
                fileWriter.append(g.getAlgTwo());
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
                    info.setAlgUsed(fields[1]);
                    info.setAlgTwo(fields[2]);
                    info.setGameWinner(fields[3]);
                    info.setNumTurnsToWin(Integer.parseInt(fields[4]));

                    aiAiGameList.add(info);
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