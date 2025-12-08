package at.ac.hcw.chess.model.utils;

import at.ac.hcw.chess.model.chessPieces.ChessPiece;

public class Move {
    private final ChessPiece piece;
    private final Position oldPosition;
    private final Position newPosition;

    public Move(ChessPiece piece, Position oldPosition, Position newPosition) {
        this.piece = piece;
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
    }

    @Override
    public String toString() {
        return this.piece.toString() + " : " + oldPosition.toString() + " -> " + this.newPosition.toString();
    }
}
