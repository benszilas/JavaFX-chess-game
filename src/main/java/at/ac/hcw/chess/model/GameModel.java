package at.ac.hcw.chess.model;

import at.ac.hcw.chess.model.chessPieces.ChessPiece;
import at.ac.hcw.chess.model.chessPieces.Queen;
import at.ac.hcw.chess.model.utils.*;

import java.util.ArrayList;

public class GameModel {
    private final ChessPieceList chessPieces;
    private final ChessPieceList promotablePieces;
    private ChessPiece selectedPiece = null;
    private final ArrayList<Move> moveHistory;
    private Color currentPlayer;

    public GameModel() {
        this.chessPieces = PopulateBoard.classicGameBoard();

        this.promotablePieces = new ChessPieceList();
        this.promotablePieces.add(new Queen(new Position(1,1), Color.BLACK));
        this.promotablePieces.add(new Queen(new Position(8,8), Color.WHITE));

        this.currentPlayer = Color.WHITE;

        this.moveHistory = new ArrayList<Move>();
    }

    public ChessPieceList getChessPieces() {
        return chessPieces;
    }

    public ChessPieceList getPromotablePieces() {
        return promotablePieces;
    }

    public ChessPiece getSelectedPiece() {
        return selectedPiece;
    }

    public void selectPiece(ChessPiece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public void selectPiece(Position position) {
        this.selectedPiece = chessPieces.getPiece(position);
    }

    public ArrayList<Move> getMoveHistory() {
        return moveHistory;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public void changePlayer() {
        if (this.currentPlayer == Color.BLACK)
            this.currentPlayer = Color.WHITE;
        else
            this.currentPlayer = Color.BLACK;
    }

    private ChessPiece[][] _2DBoard() {
        ChessPiece[][] board = new ChessPiece[8][8];
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                ChessPiece piece = this.chessPieces.getPiece(column + 1, row + 1);
                board[row][column] = piece;
            }
        }
        return board;
    }

    private void printChessBoardLine(ChessPiece[][] board, StringBuilder charBoard, int row) {
        charBoard.append(row);
        for (int column = 0; column < 8; column++) {
            if (board[row][column] != null) {
                charBoard.append(board[row][column].toString());
            } else if ((row + column & 0x1) == 0) {
                charBoard.append("□");
            } else {
                charBoard.append("■");
            }
        }
        charBoard.append(row);
        charBoard.append(System.lineSeparator());
    }

    @Override
    public String toString() {
        ChessPiece[][] board = _2DBoard();
        StringBuilder charBoard = new StringBuilder(" ABCDEFGH " + System.lineSeparator());
        for (int row = 0; row < 8; row++) {
            printChessBoardLine(board, charBoard, row);
        }
        return charBoard.toString();
    }
}
