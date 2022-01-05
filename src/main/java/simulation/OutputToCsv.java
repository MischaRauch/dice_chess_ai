package simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputToCsv {

    private static OutputToCsv singelton = null;
    private final String fileName = "filename.csv";
    // private static String dirName = "src\\main\\resources\\data";

    private OutputToCsv() {
        try {
            //  String fileName = "test.csv";
            //  File dir = new File(dirName);
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

    public static OutputToCsv getInstance() {
        if (singelton == null) {
            singelton = new OutputToCsv();
        }
        return singelton;
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

    public void writeToFile(List<String> message) {
        try {
            FileWriter myWriter = new FileWriter(fileName);

            // int counter = 0;
            // int nextLine = 0;
            for (String string : message) {
                if (string == "z") {
                    myWriter.write(System.getProperty("line.separator"));
                } else {
                    myWriter.write(string);
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


}
