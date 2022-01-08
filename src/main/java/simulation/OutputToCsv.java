package simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputToCsv {

    private List<String> trackedStates;
    private static OutputToCsv singelton = null;
    private String fileName;
    private boolean firstrun = true;
    private boolean fileCreated = false;

    public OutputToCsv(String fileName) {
        try {
            this.fileName = fileName;
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                fileCreated = true;
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static OutputToCsv getInstance(String fileName) {
        if (singelton == null) {
            singelton = new OutputToCsv(fileName);
        }
        return singelton;
    }

    public void setTrackedStates(List<String> trackedStates){
        this.trackedStates = trackedStates;
    }

    public void writeToFile(String[] message) {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            for (int i = 0; i < message.length; i++) {
                myWriter.write(message[i]);
                if (i != message.length - 1) {
                    myWriter.write(", ");
                }
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void writeToFileGame(List<String> message) {
        try {
            FileWriter myWriter = new FileWriter(fileName, true);
            //First append the headers if it's the first run:
            if (firstrun && fileCreated) {
                for (String headers : trackedStates) { //works
                    myWriter.append(headers + ", ");
                }
                firstrun = false;
            }

            myWriter.append("\n"); //new line

            for (String s : message) {
                myWriter.write(s);
                myWriter.write(", ");
            }
            // myWriter.append("\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void writeEachState(List<String> stateList){
        try {
            FileWriter myWriter = new FileWriter(fileName);

            //First append the headers:
            myWriter.append("TurnAlg, TimePerMove, Capture, PieceCount, TurnAlgTwo, TimePerMove, Capture, PieceCount \n");
            System.out.println("States array length: " + stateList.size());
            for(int i = 0; i < stateList.size()-1; i+=8){
                myWriter.write(stateList.get(i));
                myWriter.write(", ");
                myWriter.write(stateList.get(i+1));
                myWriter.write(", ");
                myWriter.write(stateList.get(i+2));
                myWriter.write(", ");
                myWriter.write(stateList.get(i+3));
                myWriter.write(", ");
                try{
                    myWriter.write(stateList.get(i+4));
                    myWriter.write(", ");
                } catch(IndexOutOfBoundsException e){
                    myWriter.write("-, ");
                }
                try{
                    myWriter.write(stateList.get(i+5));
                    myWriter.write(", ");
                } catch(IndexOutOfBoundsException e){
                    myWriter.write("-, ");
                }
                try{
                    myWriter.write(stateList.get(i+6));
                    myWriter.write(", ");
                } catch(IndexOutOfBoundsException e){
                    myWriter.write("-, ");
                }
                try{
                    myWriter.write(stateList.get(i+7));
                    myWriter.write(", ");
                } catch(IndexOutOfBoundsException e){
                    myWriter.write("-, ");
                }

                myWriter.append("\n");
            }



            myWriter.append("\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    // need a read file if we want to test for each state


}
