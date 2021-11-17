package logic.expectiminimax;

import logic.LegalMoveGenerator;
import logic.board.Board;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.game.Game;

import java.util.ArrayList;
import java.util.List;

// generates all possible states of the board for n turns ahead
public class BoardStateGenerator {

    public static ArrayList<Piece[][]> getPossibleBoardStates(Piece[][] boardPieceState) {
        Game game = Game.getInstance();
        ArrayList<Piece[][]> possibleBoardPieceStates = new ArrayList<Piece[][]>();
        // loop through all possible pieces available in game boardPieces
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                // get square at given rank and file
                Square pos = Square.getSquare(rank,file);
                if(game.getTurn()== Side.WHITE) {
                    if (boardPieceState[rank][file].getColor() == Side.WHITE) {
                        // get legal moves
                        List<Square> legalMoves = LegalMoveGenerator.getMovesWithoutState(pos, boardPieceState[rank][file]);
                        // make new board piece state for reseting
                        Piece[][] newBoardPieceState = boardPieceState;
                        for (Square s : legalMoves) {
                            // assign the square at boardpiecestate to new loc
                            newBoardPieceState[s.getRank() - 1][s.getFile()] = boardPieceState[rank][file];
                            // set old pos to empty
                            newBoardPieceState[rank][file] = Piece.EMPTY;
                            // add new state to list
                            possibleBoardPieceStates.add(newBoardPieceState);
                            // reset new board piece state
                            newBoardPieceState = boardPieceState;
                        }
                    }
                } else if (game.getTurn()== Side.BLACK) {
                    if (boardPieceState[rank][file].getColor() == Side.BLACK) {
                        // get legal moves
                        List<Square> legalMoves = LegalMoveGenerator.getMovesWithoutState(pos, boardPieceState[rank][file]);
                        // make new board piece state for reseting
                        Piece[][] newBoardPieceState = boardPieceState;
                        for (Square s : legalMoves) {
                            // assign the square at boardpiecestate to new loc
                            newBoardPieceState[s.getRank() - 1][s.getFile()] = boardPieceState[rank][file];
                            // set old pos to empty
                            newBoardPieceState[rank][file] = Piece.EMPTY;
                            // add new state to list
                            possibleBoardPieceStates.add(newBoardPieceState);
                            // reset new board piece state
                            newBoardPieceState = boardPieceState;
                        }
                    }
                }

            }
        }
        return possibleBoardPieceStates;
    }

    public static List<Integer> getPossibleBoardStatesWeights(Piece[][] boardPieceState) {
            Game game = Game.getInstance();
            //ArrayList<Piece[][]> possibleBoardPieceStates = new ArrayList<Piece[][]>();
            List<Integer> possibleBoardStatesWeights = new ArrayList<Integer>();
            // loop through all possible pieces available in game boardPieces
            for (int file = 0; file < 8; file++) {
                for (int rank = 0; rank < 8; rank++) {
                    // get square at given rank and file
                    if(game.getTurn()== Side.WHITE) {
                        Square pos = Square.getSquare(rank,file);
                        // if piece white
                        if (boardPieceState[rank][file].getColor() == Side.WHITE) {
                            // get legal moves
                            List<Square> legalMoves = LegalMoveGenerator.getMovesWithoutState(pos,boardPieceState[rank][file]);
                            // make new board piece state for reseting
                            Piece[][] newBoardPieceState = boardPieceState;
                            for(Square s : legalMoves) {
                                // assign the square at boardpiecestate to new loc
                                newBoardPieceState[s.getRank()-1][s.getFile()]=boardPieceState[rank][file];
                                // set old pos to empty
                                newBoardPieceState[rank][file]=Piece.EMPTY;
                                // add new state to list
                                int newBoardPieceStateWeights = BoardStateEvaluator.getBoardEvaluationNumber(newBoardPieceState);
                                //System.out.println("newBoardPieceStateWeights " + newBoardPieceStateWeights);
                                possibleBoardStatesWeights.add(newBoardPieceStateWeights);
                                // reset new board piece state
                                newBoardPieceState = boardPieceState;

                            }
                        }
                    } else if (game.getTurn()== Side.BLACK) {
                        Square pos = Square.getSquare(rank,file);
                        // if piece white
                        if (boardPieceState[rank][file].getColor() == Side.BLACK) {
                            // get legal moves
                            List<Square> legalMoves = LegalMoveGenerator.getMovesWithoutState(pos,boardPieceState[rank][file]);
                            // make new board piece state for reseting
                            Piece[][] newBoardPieceState = boardPieceState;
                            for(Square s : legalMoves) {
                                // assign the square at boardpiecestate to new loc
                                newBoardPieceState[s.getRank()-1][s.getFile()]=boardPieceState[rank][file];
                                // set old pos to empty
                                newBoardPieceState[rank][file]=Piece.EMPTY;
                                // add new state to list
                                int newBoardPieceStateWeights = BoardStateEvaluator.getBoardEvaluationNumber(newBoardPieceState);
                                //System.out.println("newBoardPieceStateWeights " + newBoardPieceStateWeights);
                                possibleBoardStatesWeights.add(newBoardPieceStateWeights);
                                // reset new board piece state
                                newBoardPieceState = boardPieceState;

                            }
                        }
                    }
                }
            }

            return possibleBoardStatesWeights;
    }

}
