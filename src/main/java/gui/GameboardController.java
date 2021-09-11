package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.BlackDice;
import logic.Dice;
import logic.WhiteDice;


public class GameboardController {

    @FXML
    private GridPane guiBoard;

    /// TODO Add a individual dice for each side black and white to keep track of available rolls
    // alternatively we can update FEN but there are many problems with this
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
        diceRollB.setText(Dice.roll() + "");
    }

    @FXML
    void rollW(ActionEvent event) {
        diceRollW.setText(Dice.roll() + "");
    }

    @FXML
    void initialize() {
        ///TODO implement dice color usage
//        WhiteDice whiteDice = new WhiteDice(new int[]{1,2,3,4,5,6});
//        BlackDice blackDice = new BlackDice(new int[]{1,2,3,4,5,6});

        //create all pieces
        //set event handlers
        //can access gridpane cells using row and column indices
        //indices start at 1 since row 0 and column 0 are used to display rank and file
        String[] openingMoves = {
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1",   //initial state
                "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1 1",
                "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2 1",
                "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2 5"
        };

        loadBoard(openingMoves[0]);

        guiBoard.setOnKeyPressed(event -> {
            if (event.getCode().isDigitKey())
                loadBoard(openingMoves[Integer.parseInt(event.getCode().getChar())]); //use numbers 0-3 to load board states
        });
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
     * @param p char representing the piece
     * @param row i
     * @param col j
     * @return JavaFX Scene Node specifically a VBox possibly containing a Label
     */
    public VBox createPiece(char p, int row, int col) {
        Label piece = new Label(p + "");
        piece.setFont(Font.font(42));

        VBox tile = new VBox();
        tile.setAlignment(Pos.CENTER);
        tile.getChildren().add(piece);

        ///TODO make LoadChessImages class
//        LoadChessImages loadChessImages = new LoadChessImages();
//        ImageView view = loadChessImages.loadImage(piece, p);
//        piece.setGraphic(view);

        if ((row + col) % 2 == 0) {
            //white cells: row + col % 2 == 0
            tile.setStyle("-fx-background-color: #ffffff");
            piece.setTextFill(Color.BLACK);
        } else {
            //black cells: row + col % 2 == 1
            tile.setStyle("-fx-background-color: #6b8ea2");
            piece.setTextFill(Color.WHITE);
        }


        //for inspiration lol
        tile.setOnMouseClicked(event -> {
            if (Character.isLetter(piece.getText().charAt(0))) {
                //here would be a good place to check if it matches roll number
                if (selectedPiece == null) {
                    selectedPiece = piece;
                }
            } else {
                if (selectedPiece != null) {
                    piece.setText(selectedPiece.getText());
                    selectedPiece.setText(" ");
                    selectedPiece = null;
                }
            }
        });


        return tile;
    }

    Label selectedPiece = null;

    /**
     * Should probably be part of a GameState object rather than here
     * @param fenDiceBoard String in FEN-dice notation (which i invented)
     * @return char matrix representing board. Row and Col with 0 index are empty
     */
    public char[][] parseFENd(String fenDiceBoard) {
        //chess board has starts with index 1 so to keep things simple leave index 0 empty
        char[][] board = new char[9][9];

        String[] info = fenDiceBoard.split("/|\\s"); //either split on "/" or on " " (whitespace)

        ///TODO need a proper data structure to store this stuff, maybe like a GameState object
        String activeColor = info[8];
        String castling = info[9];
        String enPassant = info[10];
        String halfmoveClock = info[11];
        String fullmoveNumber = info[12];

        ///TODO need to run a check if number rolled is valid
        int roll = Integer.parseInt(info[13]);
        diceRollB.setText(roll+"");
        diceRollW.setText(roll+"");

        for (int i = 0; i < 8; i++) {
            char[] rankSequence = info[i].toCharArray();
            char[] rank = board[i+1];
            int index = 1;
            for (char c : rankSequence) {
                if (Character.isDigit(c)) {
                    index += Character.getNumericValue(c);
                } else if (Character.isAlphabetic(c)) {
                    rank[index++] = c;
                }
            }
        }

        return  board;
    }

}
