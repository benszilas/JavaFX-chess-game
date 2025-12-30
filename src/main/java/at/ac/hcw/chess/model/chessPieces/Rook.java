package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.Position;

public class Rook extends ChessPiece {

    public Rook(Position position, Color color) {
        super(position, color);
    }

    @Override
    public String toString() {
        // Returns the unicode chess character for console display
        return (this.getColor() == Color.WHITE) ? "♖" : "♜";
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        // Rooks move in straight lines (horizontal & vertical).
        // The logic is already handled by the helper method in the parent class.
        this.possibleMoves = straightRange(piecesOnBoard);
    }
}