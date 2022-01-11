package logic.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Square {

    a8(0x70, 0), b8(0x71, 1), c8(0x72, 2), d8(0x73, 3), e8(0x74, 4), f8(0x75, 5), g8(0x76, 6), h8(0x77, 7),
    a7(0x60, 16), b7(0x61, 17), c7(0x62, 18), d7(0x63, 19), e7(0x64, 20), f7(0x65, 21), g7(0x66, 22), h7(0x67, 23),
    a6(0x50, 32), b6(0x51, 33), c6(0x52, 34), d6(0x53, 35), e6(0x54, 36), f6(0x55, 37), g6(0x56, 38), h6(0x57, 39),
    a5(0x40, 48), b5(0x41, 49), c5(0x42, 50), d5(0x43, 51), e5(0x44, 52), f5(0x45, 53), g5(0x46, 54), h5(0x47, 55),
    a4(0x30, 64), b4(0x31, 65), c4(0x32, 66), d4(0x33, 67), e4(0x34, 68), f4(0x35, 69), g4(0x36, 70), h4(0x37, 71),
    a3(0x20, 80), b3(0x21, 81), c3(0x22, 82), d3(0x23, 83), e3(0x24, 84), f3(0x25, 85), g3(0x26, 86), h3(0x27, 87),
    a2(0x10, 96), b2(0x11, 97), c2(0x12, 98), d2(0x13, 99), e2(0x14, 100), f2(0x15, 101), g2(0x16, 102), h2(0x17, 103),
    a1(0x00, 112), b1(0x01, 113), c1(0x02, 114), d1(0x03, 115), e1(0x04, 116), f1(0x05, 117), g1(0x06, 118), h1(0x07, 119),
    INVALID(99999, 99999); //arbitrary

    static Map<Integer, Square> squareMap = new HashMap<>();
    static Map<Integer, Square> boardIndexMap = new HashMap<>();

    //gets logic.board index by bit shifting, masking, and adding
    public int getIndex() {
        return ((ordinal() >> 3) << 4) + (ordinal() & 7); //(ordinal() / 8) * 16 + ordinal() % 8
    }

    //gets 0x88 square number by bit shifting, masking, and adding
    //want to investigate if bitwise operations improve performance or something
    public int get0x88() {
        return 112 - ((ordinal() >> 3) << 4) + (ordinal() & 7); //112 - (ordinal() / 8) * 16 + ordinal() % 8
    }

    static {
        for (Square s : Square.values()) {
            squareMap.put(s.squareNumber, s);
            boardIndexMap.put(s.boardIndex, s);
        }
    }

    final int squareNumber;
    final int boardIndex;

    /**
     * Private constructor for Square logic.enums. This constructor is automagically called with the values specified in the
     * enum declarations above.
     *
     * @param square 0x88 square number
     * @param index  index of square in the logic.board array
     */
    Square(int square, int index) {
        this.squareNumber = square;
        this.boardIndex = index;
    }

    public static Square getSquare(int rank, int file) {
        return squareMap.getOrDefault(rank * 16 + file, INVALID);
    }

    public static Square getSquare(int squareNumber) {
        return squareMap.getOrDefault(squareNumber, INVALID);
    }

    public static Square getSquareByIndex(int squareNumber) {
        return boardIndexMap.getOrDefault(squareNumber, INVALID);
    }

    // 1 indexed
    public static int getSquareRank(Square s) {
        int index = 0;
        for (int j = 0; j < 128; j++) {
            if (Square.getBoardIndexMap().get(j) == s) {
                index = j;
            }
        }
        int rank = 0;
        int file = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (s == Square.getSquare(j, i)) {
                    rank = j;
                    file = i;
                }
            }
        }
        return rank + 1;
    }

    // 0 indexed
    public static int getSquareFile(Square s) {
        int index = 0;
        for (int j = 0; j < 128; j++) {
            if (Square.getBoardIndexMap().get(j) == s) {
                index = j;
            }
        }
        int rank = 0;
        int file = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (s == Square.getSquare(j, i)) {
                    rank = j;
                    file = i;
                }
            }
        }
        return file;
    }

    /**
     * Gets the file of the square, where file_a = 0 and file_h = 7;
     *
     * @return integer representation of file 0-indexed
     */
    public int getFile() {
        return squareNumber & 7;
    }

    /**
     * Gets the rank of the square, not 0-indexed (rank_1 = 1, rank_8 = 8)
     *
     * @return integer corresponding to square rank
     */
    public int getRank() {
        return (squareNumber >> 4) + 1; //to make this 0-indexed, subtract 1
    }

    public static Map<Integer, Square> getSquareMap() {
        return squareMap;
    }

    public static Map<Integer, Square> getBoardIndexMap() {
        return boardIndexMap;
    }

    public int getSquareNumber() {
        return squareNumber;
    }

    public int getBoardIndex() {
        return boardIndex;
    }

    public Square getSquareRight() {
        return squareMap.getOrDefault(squareNumber + 1, INVALID);
    }

    public Square getSquareLeft() {
        return squareMap.getOrDefault(squareNumber - 1, INVALID);
    }

    public Square getLeftUp() {
        return squareMap.getOrDefault(squareNumber + 15, INVALID);
    }

    public Square getRightDown() {
        return squareMap.getOrDefault(squareNumber - 15, INVALID);
    }

    public Square getRightUp() {
        return squareMap.getOrDefault(squareNumber + 17, INVALID);
    }

    public Square getLeftDown() {
        return squareMap.getOrDefault(squareNumber - 17, INVALID);
    }

    public Square getSquareBelow() {
        return squareMap.getOrDefault(squareNumber - 16, INVALID);
    }

    public Square getSquareAbove() {
        return squareMap.getOrDefault(squareNumber + 16, INVALID);
    }

    public boolean isOffBoard(int squareNumber) {
        return (squareNumber & 0x88) != 0;
    }

    public ArrayList<Integer> getLeftDiagonals(Square square) {
        ArrayList<Integer> leftDiagonals = new ArrayList<>();
        leftDiagonals.add(square.boardIndex);
        Square currentSquare = square;

        while (!isOffBoard(currentSquare.getSquareNumber())) {
            currentSquare = currentSquare.getLeftUp();
            if (!leftDiagonals.contains(currentSquare.boardIndex)) {
                leftDiagonals.add(currentSquare.boardIndex);
            }
        }
        currentSquare = square;
        while (!isOffBoard(currentSquare.getSquareNumber())) {
            currentSquare = currentSquare.getRightDown();
            if (!leftDiagonals.contains(currentSquare.boardIndex)) {
                leftDiagonals.add(currentSquare.boardIndex);
            }
        }
        Collections.sort(leftDiagonals);
        return leftDiagonals;
    }

    public ArrayList<Integer> getRightDiagonals(Square square) {
        ArrayList<Integer> rightDiagonals = new ArrayList<>();
        rightDiagonals.add(square.boardIndex);
        Square currentSquare = square;

        while (!isOffBoard(currentSquare.getSquareNumber())) {
            currentSquare = currentSquare.getRightUp();
            if (!rightDiagonals.contains(currentSquare.boardIndex)) {
                rightDiagonals.add(currentSquare.boardIndex);
            }
        }
        currentSquare = square;
        while (!isOffBoard(currentSquare.getSquareNumber())) {
            currentSquare = currentSquare.getLeftDown();
            if (!rightDiagonals.contains(currentSquare.boardIndex)) {
                rightDiagonals.add(currentSquare.boardIndex);
            }
        }
        Collections.sort(rightDiagonals);
        return rightDiagonals;
    }


    //not really sure how to interpret what this returns
    //okay, so basically I think adding 0x77 (119) to the square difference ensures that the result is non-negative
    //so that you can use it as an index in some sort of attack table, which lets you know which pieces can attack a
    //certain square, not really sure what such a table looks like tho. Definitely more research needed
    public int getDiff(Square b) {
        return 0x77 + squareNumber - b.squareNumber;
    }

}

