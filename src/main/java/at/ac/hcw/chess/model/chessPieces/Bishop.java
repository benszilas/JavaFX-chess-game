package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.Position;

public class Bishop extends ChessPiece {

    public Bishop(Position position, Color color) {
        super(position, color);
    }

    @Override
    public String toString() {
        // Returns the unicode chess character for console display
        return (this.getColor() == Color.WHITE) ? "♗" : "♝";
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        // Bishops move diagonally.
        // The logic is handled by the diagonalRange helper in the parent class.
        this.possibleMoves = diagonalRange(piecesOnBoard);
    }
}