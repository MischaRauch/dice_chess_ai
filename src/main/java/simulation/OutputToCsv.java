package simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputToCsv {

    private List<String> trackedStates;
    private static OutputToCsv singelton = null;
    private  String fileName ;

    public OutputToCsv(String fileName) {
        try {
            this.fileName = fileName;
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
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
            FileWriter myWriter = new FileWriter(fileName);

            //First append the headers:
            for(String headers : trackedStates){ //works
                myWriter.append(headers + ", ");
            }
            myWriter.append("\n"); //new line

            for(String s : message){
                myWriter.write(s);
                myWriter.write(", ");
            }
            myWriter.append("\n");
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
            myWriter.append("Turn, TimePerMove \n");

            for(int i = 0; i < stateList.size()-1; i+=2){
                myWriter.write(stateList.get(i));
                myWriter.write(", ");
                myWriter.write(stateList.get(i+1));
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
