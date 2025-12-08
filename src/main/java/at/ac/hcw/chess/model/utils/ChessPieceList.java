package at.ac.hcw.chess.model.utils;

import at.ac.hcw.chess.model.chessPieces.ChessPiece;

import java.util.ArrayList;

public class ChessPieceList extends ArrayList<ChessPiece> {

    public Position getPosition(ChessPiece wanted) {
        for (ChessPiece piece : this) {
            if (piece == wanted) return piece.getPosition();
        }
        return null;
    }

    public ChessPiece getPiece(Position at) {
        for (ChessPiece piece : this) {
            if (piece.getPosition().getColumn() == at.getColumn() && piece.getPosition().getRow() == at.getRow()) {
                return piece;
            }
        }
        return null;
    }

    public ChessPiece getPiece(int column, int row) {
        for (ChessPiece piece : this) {
            if (piece.getPosition().getColumn() == column && piece.getPosition().getRow() == row) {
                return piece;
            }
        }
        return null;
    }
}
