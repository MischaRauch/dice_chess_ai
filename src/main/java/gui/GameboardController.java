package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.Dice;


public class GameboardController {

    public boolean whiteTurn = true;

    /// TODO Add a individual dice for each side black and white to keep track of available rolls
    // alternatively we can update FEN but there are many problems with this
    Label selectedPiece = null;
    @FXML
    private GridPane guiBoard;
    @FXML
    private Button diceRollButtonB;
    @FXML
    private Label diceRollB;
    @FXML
    private Button diceRollButtonW;
    @FXML
    private Label diceRollW;

    @FXML
    void rollB(ActionEvent event) {
        LoadChessImages aid = new LoadChessImages();
        diceRollB.setText("");
        diceRollB.setGraphic(aid.loadImage(aid.whichPiece(Dice.roll(), whiteTurn)));
        whiteTurn = true;
    }

    @FXML
    void rollW(ActionEvent event) {

        diceRollW.setText("");
        LoadChessImages aid = new LoadChessImages();
        diceRollW.setGraphic(aid.loadImage(aid.whichPiece(Dice.roll(), whiteTurn)));
        whiteTurn = false;
    }

    @FXML
    void initialize() {
        ///TODO implement dice color usage

        //create all pieces
        //set event handlers
        //can access gridpane cells using row and column indices
        //indices start at 1 since row 0 and column 0 are used to display rank and file
        String[] openingMoves = {
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1 0",   //initial state
                "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1 0 1",
                "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2 1 0",
                "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2 0 5",
                "rn1qk1nr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2 3 0" // white has no bishops
        };

        //loadBoard(openingMoves[0]);
        loadBoardAlternative(openingMoves[0]);

        guiBoard.setOnKeyPressed(event -> {
            if (event.getCode().isDigitKey())
                loadBoard(openingMoves[Integer.parseInt(event.getCode().getChar())]); //use numbers 0-3 to load board states
        });
    }

    public void loadBoardAlternative(String fenD) {
        char[][] boardState = parseFENd(fenD);
        for (int i = 1; i < boardState.length; i++) {
            for (int j = 1; j < boardState.length; j++) {
                Tile tile = new Tile(boardState[i][j], i - 1, j - 1);//0-index the row/col

                tile.setOnMouseClicked(event -> {
                    //the event handler can technically also be made in the constructor in the Tile class,
                    //but it might be better to have it here so that you can use other fields/info from this class
                    //that you would not have access to from the Tile class unless everything in here
                    //is static or if each tile has access to an instance of this class

                    //process move, check validity, update gui board, etc
                });

                guiBoard.add(tile, j, i);
            }
        }
    }

    public void loadBoard(String fenD) {
        char[][] boardState = parseFENd(fenD);
        for (int i = 1; i < boardState.length; i++) {
            for (int j = 1; j < boardState.length; j++) {
                guiBoard.add(createPiece(boardState[i][j], i, j), j, i);
            }
        }
    }

    /**
     * should be much sophisticated later with proper graphics and cool event handlers, but we gotta start somewhere
     * Maybe have a dedicated PieceFactory class or something in the future, idk
     *
     * @param p   char representing the piece
     * @param row i
     * @param col j
     * @return JavaFX Scene Node specifically a VBox possibly containing a Label
     */
    public VBox createPiece(char p, int row, int col) {
        LoadChessImages loadChessImages = new LoadChessImages();
        ImageView view = loadChessImages.loadImage(p);

        Label piece = new Label(p + "", view);
        piece.setFont(Font.font(1));

        VBox tile = new VBox();
        tile.setAlignment(Pos.CENTER);
        tile.getChildren().add(piece);

        ///TODO make LoadChessImages class
        //piece.setGraphic(view);

        if ((row + col) % 2 == 0) {
            //white cells: row + col % 2 == 0
            tile.setStyle("-fx-background-color: #d5a47d");
            piece.setTextFill(Color.BLACK);
        } else {
            //black cells: row + col % 2 == 1
            //tile.setStyle("-fx-background-color: #000000");
            tile.setStyle("-fx-background-color: #98501a");
            piece.setTextFill(Color.WHITE);
        }


        // for inspiration lol
        tile.setOnMouseClicked(event -> {
            if (Character.isLetter(piece.getText().charAt(0))) {
                //here would be a good place to check if it matches roll number
                if (selectedPiece == null) {
                    selectedPiece = piece;
                }
            } else {
                if (selectedPiece != null) {
                    piece.setText(selectedPiece.getText());
                    piece.setGraphic(loadChessImages.loadImage(selectedPiece.getText().charAt(0)));
                    selectedPiece.setText(" ");
                    selectedPiece.setGraphic(null);
                    selectedPiece = null;
                }
            }
        });

        return tile;
    }

    /**
     * Should probably be part of a GameState object rather than here
     *
     * @param fenDiceBoard String in FEN-dice notation
     * @return char matrix representing board. Row and Col with 0 index are empty
     */
    public char[][] parseFENd(String fenDiceBoard) {
        //chess board has starts with index 1 so to keep things simple leave index 0 empty
        char[][] board = new char[9][9];
        LoadChessImages loadChessImages = new LoadChessImages();
        String[] info = fenDiceBoard.split("/|\\s"); //either split on "/" or on " " (whitespace)

        ///TODO need a proper data structure to store this stuff, maybe like a GameState object
        String activeColor = info[8];
        String castling = info[9];
        String enPassant = info[10];
        String halfmoveClock = info[11];
        String fullmoveNumber = info[12];

        ///TODO need to run a check if number rolled is valid
        int rollW = Integer.parseInt(info[13]);
        // diceRollB.setText(rollW+"");
        //diceRollB.setGraphic(loadChessImages.loadImage(loadChessImages.whichPiece(rollW, whiteTurn)));
        whiteTurn = false;
        int rollB = Integer.parseInt(info[13]);
        // diceRollW.setText(rollB+"");
        //diceRollW.setGraphic(loadChessImages.loadImage(loadChessImages.whichPiece(rollW, whiteTurn)));
        whiteTurn = true;

        for (int i = 0; i < 8; i++) {
            char[] rankSequence = info[i].toCharArray();
            char[] rank = board[i + 1];
            int index = 1;
            for (char c : rankSequence) {
                if (Character.isDigit(c)) {
                    index += Character.getNumericValue(c);
                } else if (Character.isAlphabetic(c)) {
                    rank[index++] = c;
                }
            }
        }

        return board;
    }

}
