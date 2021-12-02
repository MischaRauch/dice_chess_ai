package dataCollection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import gui.controllers.ViewDataController;

public class AICsvReaderWriter extends ViewDataController {

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
                    info.setAlgUsed(fields[0]);
                    info.setAlgTwo(fields[1]);
                    info.setGameWinner(fields[2]);
                    info.setNumTurnsToWin(Integer.parseInt(fields[3]));

                    aiAiGameList.add(info); //TODO: check this
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