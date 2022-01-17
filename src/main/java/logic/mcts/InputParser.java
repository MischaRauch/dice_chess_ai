package logic.mcts;

import com.opencsv.CSVWriter;
import logic.board.Board;
import logic.enums.Piece;
import logic.enums.Side;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static logic.enums.Piece.OFF_BOARD;

public class InputParser {

    public static int[] getInputBoard(Board board, Side perspective, int diceRoll) {
        int[] b = new int[70]; //all boards + all piece types
        int index = 0;
        Piece[] x88 = board.getBoard();

        for (int i = 0; i < x88.length; i++) {
            if (x88[i] == OFF_BOARD) {
                i += 7;
            } else {
                b[index++] = x88[i].getPiecePerspective(perspective);
            }
        }

        b[64 + diceRoll - 1] = 1;

        return b;
    }

    public static String[] parse(int[] state, double prediction) {
        //String s = "" + prediction;
        String[] s = new String[71];
        s[0] = "" + prediction;
        for (int i = 1; i < s.length; i++) {
            s[i] = state[i - 1] + "";
        }

        return s;
    }

    public static String[] parseState(Board board, Side perspective, int diceRoll, double prediction) {
        return InputParser.parse(InputParser.getInputBoard(board, perspective, diceRoll), prediction);
    }

    private static final String[] HEADER = {"y", "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", "P", "N", "B", "R", "Q", "K"};


    //private static String dataSet = "/test.csv";

    public static void main(String[] args) {
        csvWriter(HEADER);

    }

    public static void logMultiGameData(String[] input, String[] header, String title) {
        try {
            File file = new File(title + ".csv");
            FileWriter writer;
            CSVWriter csvWriter;

            if (file.createNewFile()) {
                writer = new FileWriter(file);
                csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
                csvWriter.writeNext(header);
                csvWriter.writeNext(input);
            } else {
                writer = new FileWriter(file, true);
                csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
                csvWriter.writeNext(input);
            }

            csvWriter.close();
        } catch (IOException e) {
            System.out.println("what");
            e.printStackTrace();
        }
    }

    public static void csvWriter(String[] input) {

        try {
            File file = new File("trainingData_Black_vs_Basic.csv");
            FileWriter writer;
            CSVWriter csvWriter;

            writer = new FileWriter(file, true);
            csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            csvWriter.writeNext(input);
            csvWriter.close();

//            if (file.createNewFile()) {
//                writer = new FileWriter(file);
//                csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
//                csvWriter.writeNext(HEADER);
//                writer.close();
//                //csvWriter.close();
//            } else {
//                writer = new FileWriter(file);
//                csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
//                csvWriter.writeNext(input);
//                writer.close();
//                //csvWriter.close();
            //}

        } catch (IOException e) {
            System.out.println("what");
            e.printStackTrace();
        }
    }


}
