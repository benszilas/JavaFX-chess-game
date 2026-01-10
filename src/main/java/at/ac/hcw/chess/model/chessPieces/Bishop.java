package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.Position;

public class Bishop extends ChessPiece {
    public Bishop(Position position, Color color) {
        super(position, color);
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        // Bishops move diagonally
        this.possibleMoves = diagonalRange(piecesOnBoard);
    }

    @Override
    public String toString() {
        if (this.getColor() == Color.WHITE) {
            return "♗";
        }
        return "♝";
    }
}