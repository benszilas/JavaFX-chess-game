package at.ac.hcw.chess.model;

import at.ac.hcw.chess.model.chessPieces.ChessPiece;
import at.ac.hcw.chess.model.chessPieces.Queen;
import at.ac.hcw.chess.model.utils.*;

import java.util.ArrayList;

public class GameModel {
    private final ChessPieceList chessPieces;
    private final ArrayList<ChessPiece> promotablePieces;
    private ChessPiece selectedPiece = null;
    private final ArrayList<MoveRecord> moveHistory;
    private Color currentPlayer;
    private Color nextPlayer;

    public GameModel() {
        this.chessPieces = PopulateBoard.classicGameBoard();

        this.promotablePieces = new ChessPieceList();
        this.promotablePieces.add(new Queen(new Position(1,1), Color.BLACK));
        this.promotablePieces.add(new Queen(new Position(8,8), Color.WHITE));

        this.currentPlayer = Color.WHITE;
        this.nextPlayer = Color.BLACK;

        this.moveHistory = new ArrayList<MoveRecord>();
    }

    public GameModel(ChessPieceList customPieces) {
        this.chessPieces = customPieces;

        //need function to count promotable pieces
        this.promotablePieces = new ChessPieceList();
        this.promotablePieces.add(new Queen(new Position(1,1), Color.BLACK));
        this.promotablePieces.add(new Queen(new Position(8,8), Color.WHITE));

        this.currentPlayer = Color.WHITE;

        this.moveHistory = new ArrayList<MoveRecord>();
    }

    public ChessPieceList getChessPieces() {
        return chessPieces;
    }

    public ArrayList<ChessPiece> getPromotablePieces() {
        return promotablePieces;
    }

    public ChessPieceList currentUniquePromotable() {
        ChessPieceList promotionList = new ChessPieceList();
        promotablePieces.forEach(piece -> {
            if (piece.getColor() == currentPlayer
                    && promotionList.findPieces(piece.getClass(), piece.getColor()).isEmpty()) {
                promotionList.addLast(piece);
            }
        });
        return promotionList;
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

    public ArrayList<MoveRecord> getMoveHistory() {
        return moveHistory;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public Color getNextPlayer() {
        return nextPlayer;
    }

    public void changePlayer() {
        if (currentPlayer == Color.BLACK) {
            currentPlayer = Color.WHITE;
            nextPlayer = Color.BLACK;
        }
        else {
            currentPlayer = Color.BLACK;
            nextPlayer = Color.WHITE;
        }
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
        charBoard.append(row + 1);
        charBoard.append(" ");
        for (int column = 0; column < 8; column++) {
            if (board[row][column] != null) {
                charBoard.append(board[row][column].toString());
            } else if ((row + column & 0x1) == 0) {
                charBoard.append("＿");
            } else {
                charBoard.append("⛆");
            }
        }
        charBoard.append(row + 1);
        charBoard.append(System.lineSeparator());
    }

    @Override
    public String toString() {
        ChessPiece[][] board = _2DBoard();
        StringBuilder charBoard = new StringBuilder("  ＡＢＣＤＥＦＧＨ " + System.lineSeparator());
        for (int row = 0; row < 8; row++) {
            printChessBoardLine(board, charBoard, row);
        }
        charBoard.append("  ＡＢＣＤＥＦＧＨ ");
        return charBoard.toString();
    }
}
