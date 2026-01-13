package at.ac.hcw.chess.model;

import at.ac.hcw.chess.model.chessPieces.ChessPiece;
import at.ac.hcw.chess.model.chessPieces.Queen;
import at.ac.hcw.chess.model.utils.*;

import java.util.ArrayList;

public class GameModel {
    private final ChessPieceList chessPieces;
    private final ArrayList<ChessPiece> promotablePieces;
    private final ChessPieceList capturedWhitePieces;
    private final ChessPieceList capturedBlackPieces;
    private ChessPiece selectedPiece = null;
    private final ArrayList<MoveRecord> moveHistory;
    private Color currentPlayer;
    private Color nextPlayer;
    private int fullMoves;
    private int halfMoves;

    public GameModel() {
        this.chessPieces = PopulateBoard.classicGameBoard();
        this.promotablePieces = new ChessPieceList();
        this.promotablePieces.add(new Queen(new Position(1, 1), Color.BLACK));
        this.promotablePieces.add(new Queen(new Position(8, 8), Color.WHITE));

        this.capturedWhitePieces = new ChessPieceList();
        this.capturedBlackPieces = new ChessPieceList();

        this.currentPlayer = Color.WHITE;
        this.nextPlayer = Color.BLACK;

        this.moveHistory = new ArrayList<MoveRecord>();

        fullMoves = 0;
        halfMoves = 0;
    }

    public void customGame(ChessPieceList customPieces) {
        this.chessPieces.clear();
        this.chessPieces.addAll(customPieces);
    }

    public ChessPieceList getChessPieces() {
        return chessPieces;
    }

    public ChessPieceList onePlayersPieces(Color color) {
        ChessPieceList list = new ChessPieceList();
        chessPieces.forEach(chessPiece -> {
            if (chessPiece.getColor() == color)
                list.add(chessPiece);
        });
        return list;
    }

    public ArrayList<ChessPiece> getPromotablePieces() {
        return promotablePieces;
    }

    public ChessPieceList getCapturedWhitePieces() {
        return capturedWhitePieces;
    }

    public ChessPieceList getCapturedBlackPieces() {
        return capturedBlackPieces;
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
        Color color = currentPlayer;
        currentPlayer = nextPlayer;
        nextPlayer = color;
        halfMoves++;
        if ((halfMoves & 1) == 0) fullMoves++;
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

    private void printChessBoardLine(ChessPiece[][] board, StringBuilder fenString, int row) {
        int gap = 0;
        for (int column = 0; column < 8; column++) {
            if (board[row][column] != null) {
                if (gap != 0) {
                    fenString.append(gap);
                    gap = 0;
                }
                fenString.append(board[row][column].toString());
            } else {
                gap++;
            }
        }
        if (gap != 0) fenString.append(gap);
        fenString.append("/");
    }

    @Override
    public String toString() {
        String unusedFenString = " - - 0 ";
        ChessPiece[][] board = _2DBoard();
        StringBuilder fenString = new StringBuilder();
        for (int row = Position.MAX - 1; row >= 0; row--) {
            printChessBoardLine(board, fenString, row);
        }
        fenString.deleteCharAt(fenString.lastIndexOf("/"));
        fenString.append(" ");
        fenString.append(currentPlayer.toString().toLowerCase().charAt(0));
        fenString.append(unusedFenString);
        fenString.append(fullMoves);
        return fenString.toString();
    }
}
