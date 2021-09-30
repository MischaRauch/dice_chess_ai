package logic.enums;

import java.util.HashMap;
import java.util.Map;

public enum Square {

    INVALID(0x88, 128), //arbitrary

    a8(0x70, 0), b8(0x71, 1), c8(0x72, 2), d8(0x73,3), e8(0x74, 4), f8(0x75, 5), g8(0x76, 6), h8(0x77, 7),
    a7(0x60, 16), b7(0x61, 17), c7(0x62, 18), d7(0x63,19), e7(0x64, 20), f7(0x65, 21), g7(0x66, 22), h7(0x67, 23),
    a6(0x50, 32), b6(0x51, 33), c6(0x52, 34), d6(0x53,35), e6(0x54, 36), f6(0x55, 37), g6(0x56, 38), h6(0x57, 39),
    a5(0x40, 48), b5(0x41, 49), c5(0x42, 50), d5(0x43,51), e5(0x44, 52), f5(0x45, 53), g5(0x46, 54), h5(0x47, 55),
    a4(0x30, 64), b4(0x31, 65), c4(0x32, 66), d4(0x33,67), e4(0x34, 68), f4(0x35, 69), g4(0x36, 70), h4(0x37, 71),
    a3(0x20, 80), b3(0x21, 81), c3(0x22, 82), d3(0x23,83), e3(0x24, 84), f3(0x25, 85), g3(0x26, 86), h3(0x27, 87),
    a2(0x10, 96), b2(0x11, 97), c2(0x12, 98), d2(0x13,99), e2(0x14, 100), f2(0x15, 101), g2(0x16, 102), h2(0x17, 103),
    a1(0x00, 112), b1(0x01, 113), c1(0x02, 114), d1(0x03,115), e1(0x04, 116), f1(0x05, 117), g1(0x06, 118), h1(0x07, 119);

    int squareNumber;
    int boardIndex;

    /**
     * Private constructor for Square enums. This constructor is automagically called with the values specified in the
     * enum declarations above.
     * @param square 0x88 square number
     * @param index index of square in the board array
     */
    Square(int square, int index) {
        this.squareNumber = square;
        this.boardIndex = index;
    }

    static Map<Integer, Square> squareMap = new HashMap<>();

    static {
        for (Square s : Square.values())
            squareMap.put(s.squareNumber, s);
    }

    public Square getLeftDiagonals() { return null; } // add for loop here?
    public Square getRightDiagonals() { return null; }

    public Square getSquareRight() { return squareMap.getOrDefault(squareNumber+1, INVALID); } // it's like this I suppose?
    public Square getSquareLeft() { return squareMap.getOrDefault(squareNumber-1, INVALID); } // it's like this I suppose?

    public static Square getSquare(int rank, int file) {
        return squareMap.getOrDefault(rank * 16 + file, INVALID);
    }

    public static Square getSquare(int squareNumber) {
        return squareMap.getOrDefault(squareNumber, INVALID);
    }

    public int getSquareNumber() {
        return squareNumber;
    }

    public int getBoardIndex() {
        return boardIndex;
    }

    /**
     * Gets the file of the square, where file a = 0 and file h = 7;
     * @return integer representation of file 0-indexed
     */
    public int getFile() {
        return squareNumber & 7;
    }

    /**
     * Gets the rank of the square, not 0-indexed (rank 1 = 1, rank 8 = 8)
     * @return integer corresponding to square rank
     */
    public int getRank() {
        return (squareNumber >> 4) + 1; //to make this 0-indexed, subtract 1
    }

    public Square getSquareAbove() {
        return squareMap.getOrDefault(squareNumber + 16, INVALID);
    }

    //not really sure how to interpret what this returns
    public int getDiff(Square b) {
        return 0x77 + squareNumber - b.squareNumber;
    }

    public Square getSquareBelow() {
        return squareMap.getOrDefault(squareNumber - 16, INVALID);
    }


}

