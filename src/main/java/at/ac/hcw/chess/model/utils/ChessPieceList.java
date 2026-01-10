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
            if (piece.getPosition().equals(at)) {
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

    /**
     * find pieces by class and color
     * @param pieceClass Class to be found such as King.class
     * @param color the color of the pieces to find
     * @return a list of references to the found pieces. if nothing found, an empty list
     */
    public ChessPieceList findPieces(Class<?> pieceClass, Color color) {
        var foundPieces = new ChessPieceList();
        for (ChessPiece piece : this) {
            if (pieceClass.isInstance(piece) && piece.getColor() == color)
                foundPieces.add(piece);
        }
        return foundPieces;
    }

    /**
     * adds a piece to the list, if there is no piece at the same position
     * @param piece the piece to add
     */
    @Override
    public boolean add(ChessPiece piece) {
        if (piece == null)
            return false;

        ChessPiece duplicate = this.getPiece(piece.getPosition());
        if (duplicate == null){
            super.add(piece);
            return true;
        }
        return false;
    }

    /**
     * remove the piece at the specified position, if exists
     * @param position of the piece to get removed
     * @return the removed piece if successful, or null
     */
    public ChessPiece pop(ChessPiece piece) {
        ChessPiece removed = piece;
        remove(piece);
        return piece;
    }
}
