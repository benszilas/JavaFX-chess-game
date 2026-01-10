package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.Position;

public class Queen extends ChessPiece {

    public Queen(Position position, Color color) {
        super(position, color);
    }

    @Override
    public String toString() {
        // Returns the unicode chess character for console display
        return (this.getColor() == Color.WHITE) ? "♕" : "♛";
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        // The Queen combines the movement of Rooks (straight) and Bishops (diagonal).
        this.possibleMoves = straightRange(piecesOnBoard);
        this.possibleMoves.addAll(diagonalRange(piecesOnBoard));
    }
}